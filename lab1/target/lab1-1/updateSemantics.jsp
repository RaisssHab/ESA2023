<%@ page import="java.com.labs.Beans.SemanticsBean" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Semantics" %>
<html>
<head>
    <title>Update Semantics</title>
</head>
<body>

<%
    Long semanticId = Long.parseLong(request.getParameter("semanticId"));
    SemanticsBean semanticsBean = new SemanticsBean();
    Semantics updatedSemantics = semanticsBean.findSemanticsById(semanticId);
    updatedSemantics.setTranslation(request.getParameter("translation"));
    updatedSemantics.setExplanation(request.getParameter("explanation"));
    semanticsBean.updateSemantics(updatedSemantics);
%>

<h2>Semantics Updated</h2>
<p>The Semantics entry has been successfully updated.</p>
<a href="semantics">Back to Semantics List</a>

</body>
</html>
