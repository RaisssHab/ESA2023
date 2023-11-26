<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Lexicon" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lexicon List</title>
</head>
<body>
<%@ include file="menu.jsp" %>
<h2>Lexicon List</h2>

<form action="addLexicon" method="post">
    <label for="text">Text:</label>
    <input type="text" id="text" name="text" value="">

    <br>

    <label for="semanticList">Select Semantic:</label>
    <select id="semanticList" name="semanticList">
        <c:forEach var="semantic" items="${allSemantics}">
            <option value="${semantic.semanticId}" ${semantic.semanticId eq allSemantics[0].semanticId ? 'selected' : ''}>
                ${semantic.translation} - ${semantic.explanation}
            </option>
        </c:forEach>
    </select>

    <br>

    <label for="phraseList">Select Phrase:</label>
    <select id="phraseList" name="phraseList">
        <c:forEach var="phrase" items="${allPhrases}">
            <option value="${phrase.phraseId}" ${phrase.phraseId eq allPhrases[0].phraseId ? 'selected' : ''}>
                ${phrase.phrase}
            </option>
        </c:forEach>
    </select>

    <br>

    <input type="submit" value="Add">
</form>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Lexicon</th>
        <th>Phrase</th>
        <th>Translation</th>
        <th>Explanation</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="lexicon" items="${lexiconList}">
        <tr>
            <td>${lexicon.lexiconId}</td>
            <td>${lexicon.text}</td>
            <td>${lexicon.phrase.phrase}</td>
            <td>${lexicon.semantics.translation}</td>
            <td>${lexicon.semantics.explanation}</td>
            <td><a href="editLexicon?id=${lexicon.lexiconId}">Edit</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
