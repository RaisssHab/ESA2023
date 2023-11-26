<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Phrase" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Phrases List</title>
</head>
<body>
<%@ include file="menu.jsp" %>
<h2>Phrases List</h2>

<form action="addPhrase" method="post">
    <label for="phrase">Phrase:</label>
    <input type="text" id="phrase" name="phrase" value="">

    <br>

    <input type="submit" value="Add">
</form>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Phrase</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="phrases" items="${phrasesList}">
        <tr>
            <td>${phrases.phraseId}</td>
            <td>${phrases.phrase}</td>
            <td><a href="editPhrases?id=${phrases.phraseId}">Edit</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
