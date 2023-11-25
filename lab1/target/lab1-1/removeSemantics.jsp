<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored="false" %>
<%@ page import="java.util.List" %>
<%@ page import="com.labs.ORM.Semantics" %>
<%@ page import="com.labs.Beans.SemanticsBean" %>
<html>
<head>
    <title>Remove Semantics</title>
</head>
<body>

<%
    Long semanticId = Long.parseLong(request.getParameter("id"));
    SemanticsBean semanticsBean = new SemanticsBean();
    semanticsBean.removeSemantics(semanticId);
%>

<h2>Semantics Removed</h2>
<p>The Semantics entry has been successfully removed.</p>
<a href="semantics">Back to Semantics List</a>

</body>
</html>
