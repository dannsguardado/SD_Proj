<%--
  Created by IntelliJ IDEA.
  User: ritaalmeida
  Date: 24/11/16
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Authorize</title>
</head>
<body>
<script type="text/javascript">
    function goURL() {
        window.location.href = '${session.authorization}';
    }
    window.onload = goURL();
</script>
</body>
</html>
