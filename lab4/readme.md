<h1>Practical Work #4. Java Message Service </h1>
<h2>Task 1</h2>

We have added a new table with name "change_log". It consists of the following columns:
- change_id - to differentiate rows;
- change_type - CREATE/UPDATE/DELETE - describes what type of change is made to the table;
- entity_class - the name of an entity being considered (lexicon/phrases/semantics);
- entity_id - the id of an entity being considered;
- field_name - if change_type is CREATE or UPDATE - each row describes changes to each field of an entity - here the name of the modified field is being considered;
- new_value - if change_type is CREATE or UPDATE - each row describes changes to each field of an entity - here the new value of the modified field is being considered.

<h2>Task 2</h2>

For this task we have to create and configure a ConnectionFactory instance and a Destination instance. The destination object can be either a query or a topic. As in the future we are going to implement two message listeners which will have to react to the same message sent to destination, the type of the destination we should choose is a topic.

The created ConnectionFactory description:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/5cd751bd-e505-431c-add8-9bd66bfaf33a)

The created Topic description:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/0855019e-59f4-4d1f-a79a-27b944cddb62)

<h2>Task 3</h2>

To achieve the goal of being able to send messages to the destination, we create a MessageSender class and mark it as a Spring component. We autowire a JmsTemplate instance which allows us to send a message to the destination (the destination is configured for the JmsTemplate bean). 

Our MessageSender requires several parameters to send a message:
- a change type;
- an entity class;
- an entity id;
- a map in the format String : Object where for each modified field (String) an updated value (Object) is provided.

 As we can see, the first three parameters are common for all operations and therefore always required. The fourth parameter in the form of a map can be empty if the type of the operation doesn't include modifying fields (DELETE) or if the update doesn't concern a certain field of an entity.

The message being sent to the destination has the type MapMessage. We can't use primitive types to send such complex structured data, and we can't define an Object for this as well due to the restrictions imposed on what can be carried as an Object message.

What we do is basically build a new instance of MapMessage with the data provided and send it to the destination.

``` java

public void sendMessage(ChangeType changeType, String entityClass, Long entityId, Map<String, Object> fields) {
    logger.info("sending a message");
    
    jmsTemplate.send(new MessageCreator(){
            @Override
            public Message createMessage(Session session) throws JMSException {
                
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("changeType", changeType.toString());
                mapMessage.setString("entityClass", entityClass);
                mapMessage.setLong("entityId", entityId);

                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    mapMessage.setObject(key, value);
                }
                
                return mapMessage;
            }
        });
}
```

Now to the part related to making controller methods send a message to the destination.

We will have to prepare data to use it with our MessageSender. That's why convenience classes are created for each basic entity: LexiconMapper, PhraseMapper, SemanticsMapper and a separate MapDifferentiator class. Let's go in details.

LexiconMapper, PhraseMapper and SemanticsMapper allow us, based on an entity (and sometimes the entities that are related to them through a foreign key - that's the case for the LexiconMapper), to get a Map of fields with their corresponding values. These are the fields that can be modified.

A simple example:

``` java

public class SemanticsMapper {
    public static Map<String, Object> map(Semantics semantics) {
        Map<String, Object> map = new HashMap<>();
        map.put("semanticId", semantics.getSemanticId());
        map.put("translation", semantics.getTranslation());
        map.put("explanation", semantics.getExplanation());
        return map;
    }
}

```

MapDifferentiator class is supposed to be used when an entity is updated, not created or deleted. It takes two maps and returns a new map with entries with keys where the values have been updated with the second provided map.

The implementation:

``` java

public class MapDifferentiator {
    public static Map<String, Object> differentiate(Map<String, Object> map1, Map<String, Object> map2) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map2.entrySet()) {
            String key = entry.getKey();
            Object value2 = entry.getValue();
            Object value1 = map1.get(key);

            if (value2 == null && value1 == null) {
                continue;
            } else if (value2 == null && value1 != null) {
                result.put(key, value2);
                continue;
            }

            if (!map1.containsKey(key) || !value2.equals(value1)) {
                result.put(key, value2);
            }
        }
        return result;
    }
}

```

We can see that there is a special treatment for null values and situations when the first map doesn't contain the same key as the second map.

We don't make much changes to the controller implementations, except for methods related to updating an entity. Here we need an old entity (before it was updated), and we don't have the opportunity to get it when the update method from a service is called. That's why we pass it as an additional parameter to the service method.

``` java

@PutMapping
public ResponseEntity<Object> updateLexicon(@RequestBody LexiconRequest newLexicon, @RequestHeader("Accept") String acceptHeader) {
    Lexicon updatedLexicon = lexiconService.getLexiconById(newLexicon.getLexiconId());
    // pay attention here
    Map<String, Object> oldLexiconMap = LexiconMapper.map(updatedLexicon, updatedLexicon.getPhrase(), updatedLexicon.getSemantics()); 
    updatedLexicon.setText(newLexicon.getText());
    updatedLexicon.setSemantics(semanticsService.findSemanticsById(newLexicon.getSemanticId()));
    updatedLexicon.setPhrase(phrasesService.findPhrasesById(newLexicon.getPhraseId()));

    // and here
    lexiconService.updateLexicon(updatedLexicon, oldLexiconMap); 

    return Helper.response(updatedLexicon, acceptHeader, "/lexicon.xslt", Lexicon.class);
}

```

For all service methods except related to retrieving from a database and deleting, we first persist/merge the new entity and then flush it to make changes to the database and update the entity. Updated entity gets a new identifier which is solved on the database level, and this is how it was decided to get an updated entity. 

Then, for creating/deleting objects, we just use a mapper for an entity and convert it to a map of fields, which is then sent to the MessageSender instance. For updating objects, we first use MapDifferentiator to leave only the chagned fields.

Examples:

``` java

public void createLexicon(Lexicon lexicon) {
    entityManager.persist(lexicon);
    entityManager.flush();
    messageSender.sendMessage(ChangeType.CREATE, "lexicon", lexicon.getLexiconId(), 
                            LexiconMapper.map(lexicon, lexicon.getPhrase(), lexicon.getSemantics()));
}

```

``` java

public void updateLexicon(Lexicon lexicon, Map<String, Object> oldLexiconMap) {
    entityManager.merge(lexicon);
    entityManager.flush();
    messageSender.sendMessage(ChangeType.UPDATE, "lexicon", lexicon.getLexiconId(),
                            MapDifferentiator.differentiate(oldLexiconMap, LexiconMapper.map(lexicon, lexicon.getPhrase(), lexicon.getSemantics())));
}

```

<h2>Task 4</h2>

We created an ORM object to work with the change_log table. We also created the ChangeLogService class with the only method to add a new record to the database.

Then we implemented the LogMessageListener class with a method with a Message parameter. The class autowires the ChangeLogService instance and can use it. In the method, the fields from a MapMessage instance are transformed into ChangeLog fields. There are two main cases: when the change type is DELETE and when it's not. In the former we don't have to put a map of entities fields, only the required fields. In the latter, we use a loop to add several rows corresponding to each updated field.

LogMessageListener can extend javax.jms.Message listener, but here we use a Spring feature - the annotation JmsListener that can be used on any method, with destination and container factory specified.  

<h2>Task 5</h2>

<h2>Task 6</h2>

<h2>Task 7</h2>

<h2>Demonstration</h2>

