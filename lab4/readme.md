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

We will make notifications when certain entity classes are modified. So our condition is the name of the entity.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/a2f21a69-67ae-4b3b-aa4e-2a601be3082c)

<h2>Task 6</h2>

We created an ORM object to work with the notification table. We also created the NotificationService class with the only method to get a record for the required entity name.

Then we created the EmailConfiguration class with the JavaMailSender bean. It allows to send email messages. We also created the EmailService class with two methods. The first method, sendSimpleMessage, is a convenience method that can send message using only the typical information a user enters to send a message - a receiver's email, a subject, and the text of a message. The second method, sendLogMessage, is a method with a prepared text that only needs to be fitted with the information about the receiver's email, the change type, the entity name and the entity id.

``` java
public void sendLogMessage(String to, String changeType, String entityClass, Long entityId) {
    StringBuilder sb = new StringBuilder();

    sb.append("Hello! A change has been made to the " + entityClass + " with id = " + entityId + ".\n\n");
    sb.append("Type of change: " + changeType + ".\n\n");
    sb.append("--\n");
    sb.append("There's no need to reply to this message.\n");

    // Convert StringBuilder to String
    String result = sb.toString();

    sendSimpleMessage(to, "A change to an entity", result);

}
```

Then we implemented the EmailMessageListener class with a method with a Message parameter. The class autowires the NotificationService instance and can use it. In the method, the fields from a MapMessage instance are used to get a Notification entry from the database corresponding to the provided entity name and to send a message using the sendLogMessage method from the EmailService class. The listener doesn't do anything in case if an entity name wasn't found in the database.

``` java
...
String changeType = mapMessage.getString("changeType");
String entityClass = mapMessage.getString("entityClass");
Long entityId = mapMessage.getLong("entityId");

Notification notification;

try {
    notification = notificationService.getNotificationByEntityName(entityClass);
} catch (NoResultException ex) {
    logger.info("No address found for this entity: " + entityClass);
    return;
}
emailService.sendLogMessage(notification.getEmail(), changeType, entityClass, entityId);
...
```

<h2>Task 7</h2>

Making everything work together is actually a part that took a couple of tens of hours. There were two main problems, and the majority of time was spend for the second one.

<h3> The first and quite easy to solve issue </h3>
The first problem was that the topic acted itself like a queue and only one random listener "reacted" to the message. I'm not exactly sure why messages came at all, but it was certainly due problems with the configuration of the ConnectionFactory and the Destination. At first it was this way:

``` xml
<bean id="connectionFactory" class="org.springframework.jndi.JndiObjectFactoryBean">
    <property name="jndiName" value="jms/ConnectionFactory"/>
</bean>
<bean id="destination" class="org.springframework.jndi.JndiObjectFactoryBean">
    <property name="jndiName" value="jms/topic"/>
</bean>
```

No errors detected by the application server - and this therefore didn't rise any suspicions. Eventually I tried to use an annotation style:

``` java
@Configuration
@EnableJms
@ComponentScan(basePackages="com.labs.jms")
public class JmsConfig {
    
    @Bean
    public ConnectionFactory connectionFactory() {
        JndiObjectFactoryBean connectionFactory = new JndiObjectFactoryBean();
        connectionFactory.setJndiName("jms/ConnectionFactory");
        connectionFactory.setResourceRef(true);
        
        try {
            connectionFactory.afterPropertiesSet();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return (ConnectionFactory) connectionFactory.getObject();
    }

    @Bean
    public Destination destination() {
        JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
        jndiObjectFactoryBean.setJndiName("jms/topic");
        jndiObjectFactoryBean.setResourceRef(true);
        try {
            jndiObjectFactoryBean.afterPropertiesSet();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        return (Destination) jndiObjectFactoryBean.getObject();
    }
...
}
```

... and it yielded the same result. I came to conclusion that JndiObjectFactoryBean is not a working way to do what I want and resorted to a very straightforward approach:

``` java
@Configuration
@EnableJms
@ComponentScan(basePackages="com.labs.jms")
public class JmsConfig {
    
    @Bean
    public ConnectionFactory connectionFactory() {

        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        // lookup the queue connection factory
        ConnectionFactory connFactory;
        try {
            connFactory = (ConnectionFactory) ctx.lookup("jms/ConnectionFactory");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        return connFactory;
    }

    @Bean
    public Destination destination() {
        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (Destination) ctx.lookup("jms/topic");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
 ...
}
```

... and it worked this time!

After some time passed, I found a code with a xml config, which was almost the same as mine, but my version lacked JNDI template! Including it solves the issue as well:

``` xml
<bean id="jndiTemplate" class="org.springframework.jndi.JndiTemplate">
    <property name="environment">
        <props>
            <prop key="java.naming.factory.initial">com.sun.enterprise.naming.SerialInitContextFactory</prop>
            <prop key="java.naming.factory.url.pkgs">com.sun.enterprise.naming</prop>
            <prop key="java.naming.factory.state">com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl</prop>
        </props>
    </property>
</bean>

<bean id="connectionFactory" class="org.springframework.jndi.JndiObjectFactoryBean">
    <property name="jndiTemplate" ref="jndiTemplate"/>
    <property name="jndiName" value="jms/ConnectionFactory"/>
</bean>
<bean id="destination" class="org.springframework.jndi.JndiObjectFactoryBean">
    <property name="jndiTemplate" ref="jndiTemplate"/>
    <property name="jndiName" value="jms/topic"/>
</bean>
```

<h3> The second and very time consuming to solve (for me) issue </h3>

It's duplicate messages! Each listener received a message exactly twice. At first I thought that the problem is with the sender, exactly with the JMS template and that I should try rewriting it some other way.

The first way was using the ConnectionFactory autowired to the MessageSender class:

``` java
try {
    Connection connection = connectionFactory.createConnection();
    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    
    MessageProducer publisher = session.createProducer(topic);
    //publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    MapMessage mapMessage = session.createMapMessage();
    mapMessage.setString("changeType", changeType.toString());
    mapMessage.setString("entityClass", entityClass);
    mapMessage.setLong("entityId", entityId);

    for (Map.Entry<String, Object> entry : fields.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();

        mapMessage.setObject(key, value);
    }

    publisher.send(mapMessage);

    publisher.close();
    session.close();
    connection.close();
    
    
    System.out.println("Message sent.");
} catch (JMSException ex) {
    logger.log(Level.SEVERE, null, ex);
}
```

... yielding the same result.

Maybe there's a problem with the connection factory again? And there's the second way:

``` java
...
InitialContext cntxt = new InitialContext();
ConnectionFactory connectionFactory = (ConnectionFactory) cntxt.lookup("jms/ConnectionFactory");
// everything else is the same
```

I also tried to experiment with the type of ConnectionFactory - it can be configures TopicConnectionFactory. It didn't help.

Maybe the problem is not with the sender? I tried to see the information about the message received by listeners:

``` java
logger.info("my ID: " + listenerId.toString());
MapMessage mapMessage = (MapMessage) message;
logger.info("JMS Correlation ID: " + mapMessage.getJMSCorrelationID());
logger.info("JMS Message ID: " + mapMessage.getJMSMessageID());
logger.info("JMS Delivery Mode: " + String.valueOf(mapMessage.getJMSDeliveryMode()));
logger.info("JMS Destination: " + mapMessage.getJMSDestination().toString());
logger.info("JMS Redeliviered: " + String.valueOf(mapMessage.getJMSRedelivered()));
```

... and it turned out that the message id was the same, and it also wan't a problem with redelivery - the messages weren't redelivered, and most importantly - listener ids were DIFFERENT!

So we get two instances of listeners instead of one instance? Let's see...

``` java
private static Boolean isInitialized = false;

public LogMessageListener() {
    logger.info("logmessagelistener instantiated");
    synchronized(this) {
        if (isInitialized) {
            throw new InstantiationError("already initialized once");
        }
        isInitialized = true;
    }
}
```

Let's try to deploy it with such a constructor...

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/1e0f3171-28e9-4456-bf78-404117ab8f75)

How is it possible?

The possible explanation is provided here: https://stackoverflow.com/questions/18265540/spring-beans-getting-initialized-twice-spring-integration. The best answer says:

>Spring MVC Apps generally have 2 contexts; the servlet context and the root context.
>
> It's generally good practice to put your "web" beans (@Controllers, views, Http inbound adatpers etc) in the servlet context and all the "business" beans in the root context.
>
> Instead of importing your beans, you should put them in the root context using the context loader listener.

We created a separate JMS xml configuration (it was earlier in the same file with mvc/rest configuration) and modified the web.xml file from 

``` xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/mvc-config.xml</param-value>
</context-param>
```

to

``` xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/mvc-config.xml, classpath:META-INF/jms.xml</param-value>
</context-param>
```

... and it finally got to work!

<h2>Demonstration</h2>

Let's create, modify and remove a semantics and see whether changelog changes and an email gets sent.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/635e1aa6-3429-4258-9426-243ea4c873ad)

change_log:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/273a440f-14c1-4369-8b47-de13997571e4)

An email:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/43b2768a-9409-4577-8733-8c94b6335c03)

Let's make a change to both fields, then to only one field, then to no fields and track what happens.

Both fields:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/9d790260-96eb-4f52-af0b-f3339425649f)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/25be70ef-7194-4fb9-af18-988ff1b649cc)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/94390937-79b7-4e5d-ae0b-c4709c50000b)

One field:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/44507bf7-7d5b-47fd-bcca-8ba823afa866)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/77fbeac2-c607-4448-80e7-02088a5deea9)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/ea739be4-90e9-467a-ba2b-77259b7a80d9)

We get only one change in the database, as expected.

No fields:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/c6406bf8-6933-40d6-824c-394dc172c94c)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/8b5a84ca-5920-45a9-b6f0-cb70a67be607)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/f321dabd-21bb-4ee3-9c64-01a09f63a458)

We got no changes in the database, but received a message - because we don't check if fields are empty. That's better to be avoided, but okay.

Now deletion:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/8ffe78cf-4f47-4e12-8365-06d99542c63e)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/e5d767ef-a146-476e-8a79-58353e86c9d6)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/4793bbf4-d296-4062-bcc0-101999be87a2)

Let's check where a message comes when there's an update to phrases:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/08226b49-7083-4a26-8e11-d7f8048d13fa)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/b0a26ecf-c1a4-4f26-b6d7-65fb31b63c5e)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/5c6f81e6-61db-4018-99dc-2836d39166de)

And it came where it was supposed to - to another account.

What about changes to lexicon?

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/eacebcd4-3705-4e53-b534-ea6baaca24b6)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/b8211026-f4c9-446e-80b3-38381a650ca8)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/02f668ed-ced3-4deb-8a76-913c065c86a2)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/165fdd8e-af50-4c89-81e8-8011cb42d939)

The message wasn't received on any of specified emails.




