<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Lexicon" %>
<%@ page import="com.labs.ORM.Semantics" %>
<%@ page import="com.labs.ORM.Phrase" %>
<%@ page import="com.labs.Beans.LexiconBean" %>
<%@ page import="com.labs.Beans.PhraseBean" %>
<%@ page import="com.labs.Beans.SemanticsBean" %>
<html>
<head>
    <title>Update Lexicon</title>
</head>
<body>

<%
    Long lexiconId = Long.parseLong(request.getParameter("lexiconId"));
    LexiconBean lexiconBean = new LexiconBean();
    Lexicon updatedLexicon = lexiconBean.findLexiconById(lexiconId);

    SemanticsBean semanticsBean = new SemanticsBean();
    PhraseBean phraseBean = new PhraseBean();

    updatedLexicon.setText(request.getParameter("text"));
    updatedLexicon.setSemantics(semanticsBean.findSemanticsById(Long.parseLong(request.getParameter("semanticList"))));
    updatedLexicon.setPhrases(phraseBean.findPhrasesById(Long.parseLong(request.getParameter("phraseList"))));

    lexiconBean.updateLexicon(updatedLexicon);
%>

<h2>Lexicon Updated</h2>
<p>The Lexicon entry has been successfully updated.</p>
<a href="lexicon">Back to Lexicon List</a>

</body>
</html>
