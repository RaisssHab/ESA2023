<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Lexicon" %>
<%@ page import="com.labs.Beans.LexiconBean" %>
<html>
<head>
    <title>Remove Lexicon</title>
</head>
<body>

<%
    Long lexiconId = Long.parseLong(request.getParameter("id"));
    LexiconBean lexiconBean = new LexiconBean();
    lexiconBean.removeLexicon(lexiconId);
%>

<h2>Lexicon Removed</h2>
<p>The Lexicon entry has been successfully removed.</p>
<a href="lexicon">Back to Lexicon List</a>

</body>
</html>
