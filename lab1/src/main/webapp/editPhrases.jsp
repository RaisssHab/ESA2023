<%@ page import="com.labs.ORM.Phrase" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Edit Phrases</title>
</head>
<body>

<h2>Edit Phrases</h2>

<form action="updatePhrases" method="post">
    <input type="hidden" name="phraseId" value="${phrases.phraseId}">

    <label for="phrase">Phrase:</label>
    <input type="text" id="phrase" name="phrase" value="${phrases.phrase}">

    <br>

    <input type="submit" value="Save">
</form>

<a href="removePhrases?id=${phrases.phraseId}">Remove</a>
<a href="phrases">Back to Phrases List</a>
</body>
</html>
