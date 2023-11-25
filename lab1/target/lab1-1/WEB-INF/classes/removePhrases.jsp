<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Phrases" %>
<%@ page import="com.labs.Beans.PhrasesBean" %>
<html>
<head>
    <title>Remove Phrases</title>
</head>
<body>

<%
    Long phraseId = Long.parseLong(request.getParameter("id"));
    PhraseBean phraseBean = new PhraseBean();
    phraseBean.removePhrases(phraseId);
%>

<h2>Phrases Removed</h2>
<p>The Phrases entry has been successfully removed.</p>
<a href="phrases">Back to Phrases List</a>

</body>
</html>
