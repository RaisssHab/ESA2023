<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Semantics" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Semantics</title>
</head>
<body>

<h2>Edit Semantics</h2>

<form action="updateSemantics" method="post">
    <input type="hidden" name="semanticId" value="${semantics.semanticId}">
    
    <label for="translation">Translation:</label>
    <input type="text" id="translation" name="translation" value="${semantics.translation}">
    
    <br>

    <label for="explanation">Explanation:</label>
    <textarea id="explanation" name="explanation">${semantics.explanation}</textarea>

    <br>

    <input type="submit" value="Save">
</form>

<a href="removeSemantics?id=${semantics.semanticId}">Remove</a>
<a href="semantics">Back to Semantics List</a>
</body>
</html>
