<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Semantics" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Semantics List</title>
</head>
<body>
<%@ include file="menu.jsp" %>
<h2>Semantics List</h2>

<form action="addSemantics" method="post">
    <label for="translation">Translation:</label>
    <input type="text" id="translation" name="translation" value="">
    
    <br>

    <label for="explanation">Explanation:</label>
    <textarea id="explanation" name="explanation"></textarea>
    
    <br>

    <input type="submit" value="Add">
</form>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Translation</th>
        <th>Explanation</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="semantics" items="${semanticsList}">
        <tr>
            <td>${semantics.semanticId}</td>
            <td>${semantics.translation}</td>
            <td>${semantics.explanation}</td>
            <td><a href="editSemantics?id=${semantics.semanticId}">Edit</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
