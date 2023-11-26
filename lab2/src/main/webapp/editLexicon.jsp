<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Lexicon" %>
<%@ page import="com.labs.ORM.Semantics" %>
<%@ page import="com.labs.ORM.Phrase" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Lexicon</title>
</head>
<body>

<h2>Edit Lexicon</h2>

<form action="updateLexicon" method="post">
    <input type="hidden" name="lexiconId" value="${lexicon.lexiconId}">

    <label for="text">Text:</label>
    <input type="text" id="text" name="text" value="${lexicon.text}">

    <br>

    <label for="semanticList">Select Semantic:</label>
    <select id="semanticList" name="semanticList">
        <c:forEach var="semantic" items="${allSemantics}">
            <option value="${semantic.semanticId}" ${semantic.semanticId eq lexicon.semantics.semanticId ? 'selected' : ''}>
                ${semantic.translation} - ${semantic.explanation}
            </option>
        </c:forEach>
    </select>

    <br>

    <label for="phraseList">Select Phrase:</label>
    <select id="phraseList" name="phraseList">
        <c:forEach var="phrase" items="${allPhrases}">
            <option value="${phrase.phraseId}" ${phrase.phraseId eq lexicon.phrase.phraseId ? 'selected' : ''}>
                ${phrase.phrase}
            </option>
        </c:forEach>
    </select>

    <br>

    <input type="submit" value="Save">
</form>

<a href="removeLexicon?id=${lexicon.lexiconId}">Remove</a>
<a href="lexicon">Back to Lexicon List</a>
</body>
</html>
