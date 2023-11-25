<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ page import="com.labs.ORM.Phrases" %>
<%@ page import="com.labs.Beans.PhraseBean" %>
<html>
<head>
    <title>Update Phrases</title>
</head>
<body>

<%
    Long phraseId = Long.parseLong(request.getParameter("phraseId"));
    PhraseBean phrasesBean = new PhraseBean();
    Phrase updatedPhrases = phrasesBean.findPhrasesById(phraseId);
    updatedPhrases.setPhrase(request.getParameter("phrase"));
    phrasesBean.updatePhrases(updatedPhrases);
%>

<h2>Phrases Updated</h2>
<p>The Phrases entry has been successfully updated.</p>
<a href="phrases">Back to Phrases List</a>

</body>
</html>
