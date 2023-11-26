<h1>Practical Work #1. Application with common JavaEE architecture </h1>
<h2>Task 1</h2>
This is how the Glassfish application server can be started:

```
asadmin start-domain
```

Here's an example:
```
D:\Apps\glassfish5\bin>asadmin start-domain
Waiting for domain1 to start ..................
Successfully started the domain : domain1
domain  Location: D:\Apps\glassfish5\glassfish\domains\domain1
Log File: D:\Apps\glassfish5\glassfish\domains\domain1\logs\server.log
Admin Port: 4848
Command start-domain executed successfully.
```

This is how the application server can be stopped:

```
asadmin stop-domain
```

There's also a way to restart the application server:

```
asadmin restart-domain
```

<h2>Task 2</h2>
The database of our choice is PostgreSQL 16.

<h2>Task 3</h2>

Our database will consist of the following entities: Semantics, Phrases and Lexicon.
A Phrase is a text and typically represents a sentence. An example of Lexicon is a word or a collocation of a certain Phrase with a specific Semantics. 
The idea is that a word or a collocation may have different meanings depending on the context, that's why Semantics and Lexicon are different entities and why a link with a Phrase is required.

Note: a Lexicon is dependent on Semantics and Phrases and therefore deleting entries in the latter may lead to database errors - we know and accept this fact, but don't do anything about it in the application in order to simplify it.

Here's the script to create create the schema and the tables of the selected model:

```
DROP TABLE IF EXISTS LexiconAnalysis.lexicon;
DROP TABLE IF EXISTS LexiconAnalysis.semantics;
DROP TABLE IF EXISTS LexiconAnalysis.phrases;

CREATE SCHEMA IF NOT EXISTS LexiconAnalysis;

SET search_path TO LexiconAnalysis;

CREATE TABLE semantics (
    semantic_id SERIAL PRIMARY KEY,
    translation VARCHAR(255),
    explanation TEXT
);

CREATE TABLE phrases (
    phrase_id SERIAL PRIMARY KEY,
    phrase TEXT
);

CREATE TABLE lexicon (
    lexicon_id SERIAL PRIMARY KEY,
    semantic_id INT REFERENCES semantics (semantic_id),
    phrase_id INT REFERENCES phrases (phrase_id),
    text VARCHAR(255)
);
```

<h2>Task 4</h2>

To create the data layer we used the following annotations:

- @Entity - to mark a class as a database entity;
- @Id - to set a field as a PK;
- @GeneratedValue - to set the identity strategy for generation of PK values;
- @Column - to mark a field as a table field where the writing is not obvious, like here:

```
@Column(name = "lexicon_id")
    private Long lexiconId;
```
- @ManyToOne - to work with FK fields.

The data layer classes are located in com.labs.ORM (Lexicon, Phrase, Semantics, the other two classes should be ignored).

<h2>Task 5</h2>

We implemented stateless session beans (@Stateless). All beans use EntityManager objects injected using @PersistenceContext. The beans implement four basic operations:
- add an object;
- remove an object;
- find an object by its identifier;
- get all objects.

The business layer classes are located in com.labs.Beans (LexiconBean, PhraseBean, SemanticsBean).

<h2>Task 6</h2>

The view layer was implemented using Servelts and JSP files. 

There are the following types of servlets:
- LexiconServlet, PhrasesServlet, SemanticsServlet - to see the table's contents;
- Edit*Servlet - to edit a specific entry, it basically provides a page to edit an entry;
- Update*Servlet - to update a specific entry, it basically updates an entry and then redirects to a success page;
- Remove*Servlet - to remove a specific entry, it basically removes an entry and then redirects to a success page;
- Add*Servlet - to add a specific entry, it basically adds an entry and then redirects to a page with table's contents (see item 1 of this list).

There are the following JSP-files:
- lexicon.jsp, phrases.jsp, semantics.jsp - to see the table's contents;
- editLexicon.jsp, editPhrases.jsp, editSemantics.jsp - to edit the table's contents;
- actionSuccess.jsp - to inform about the success of an operation (update, remove);
- menu.jsp - a menu consisting of three links which is included in pages from item 1 of this list.

An example of code to use a JSP file:

```
RequestDispatcher dispatcher = request.getRequestDispatcher("/editLexicon.jsp");
dispatcher.forward(request, response);
```

An example of code to redirect to another page:

```
response.sendRedirect(request.getContextPath() + "/phrases");
```

To represent all entries of a table we used a loop:

```
<c:forEach var="phrases" items="${phrasesList}">
    <tr>
        <td>${phrases.phraseId}</td>
        <td>${phrases.phrase}</td>
        <td><a href="editPhrases?id=${phrases.phraseId}">Edit</a></td>
    </tr>
</c:forEach>
```

We used information from forms to extract information to insert/update/remove entries. Here's an example of a form:

```
<form action="updatePhrases" method="post">
    <input type="hidden" name="phraseId" value="${phrases.phraseId}">

    <label for="phrase">Phrase:</label>
    <input type="text" id="phrase" name="phrase" value="${phrases.phrase}">

    <br>

    <input type="submit" value="Save">
</form>
```

We can notice the use of "name" attribute in input tags. It helps us to extract information later in servlets:

```
Long phraseId = Long.parseLong(request.getParameter("phraseId"));
Phrase updatedPhrases = phraseBean.findPhrasesById(phraseId);
updatedPhrases.setPhrase(request.getParameter("phrase"));
```

That's how we included 'menu.jsp' contents to other files:

```
<%@ include file="menu.jsp" %>
```

That's how actionSuccess.jsp which can state success for different types of operations and tables:

```
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>${actionNameInf} ${name}</title>
</head>
<body>

<h2>${name} ${actionNamePast}</h2>
<p>The ${name} entry has been successfully ${actionNamePast}.</p>
<a href="${returnLink}">Back to ${name} List</a>

</body>
</html>
```

<h2>Task 7</h2>

In order to make everything work together, we should 
- add our database to the application server;
- configure our application to use a specific database;
- to make a .war package of our application;
- to deploy the application to the server.

To work with the server, we use the GUI version available on http://localhost:4848/.

<h3>Adding our database to the application server</h3>
First we configured a connection pool.

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/1820b24a-7252-4de5-bc7a-9a19d07ff9a2)

And then a resource:
![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/9f787e17-47fb-4008-a7d0-339e709d16ff)

We must use the JNDI name configured here in our application configuration to access the database.

<h3>Configuring our application to use a specific database</h3>

In persistence.xml:

```
<persistence-unit name="myPersistence" transaction-type="JTA">
    <jta-data-source>jdbc/postgtes</jta-data-source>
</persistence-unit>
```

<h3>Making a .war package</h3>

```
mvn clean install
```

<h3>Deploying the application to the server</h3>

```
asadmin deploy "...\lab1\target\lab1-1.war"
```

It's also available from the admin panel.

<h2>Demonstration</h2>

That's what we see when we visit the main page:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/6b1089f6-cfb9-4b59-851b-989dd45de4f4)

We can't add a lexicon entry without a phrase and a semantics. Let's create a couple for each:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/fbdfe4a2-ee13-4645-947e-96069995571c)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/4e503ece-e6f0-47f8-a736-066deeb21f01)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/384e05a5-757e-430d-b831-9c0fc2262609)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/f0650b2e-bbea-4aed-9dd3-84cb981f7f84)

Now we can do the following:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/6e04579d-53d9-4a23-b9e4-7bc9c0ab6dea)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/5fa81676-9126-493e-a97b-20f2b495e1db)

Suppose we didn't like the first entry. Let's edit it:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/755f5200-ddda-4adc-b087-c18817078c8e)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/f721c595-5c0e-4f17-872e-178ed939d9a1)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/8b6d24b6-02ba-4a01-98af-90419640f2e2)

We now can delete the second entry:

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/1919e1bc-71b7-4c82-8886-528e0f50152c)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/ecd848b3-a121-4bbf-9514-a0b405947c1d)

![изображение](https://github.com/RaisssHab/ESA2023/assets/60664914/941b4799-ec15-4368-a6bd-8cb7d7d90c50)










