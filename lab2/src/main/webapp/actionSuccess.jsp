<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${actionNameInf} ${name}</title>
</head>
<body>

<h2>${name} ${actionNamePast}</h2>
<p>The ${name} entry has been successfully ${actionNamePast}.</p>
<a href="${returnLink}">Back to ${name} List</a>

</body>
</html>
