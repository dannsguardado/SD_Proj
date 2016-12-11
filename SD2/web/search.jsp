<%--
  Created by IntelliJ IDEA.
  User: ritaalmeida
  Date: 28/11/16
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search Auction</title>
</head>
<body>
<form action="searchauction" method="post">
    <input id="code" type="number" class="form-control" placeholder="Code" name="Code" required/><br>
    <input type="submit" class="btn btn-primary" method="execute" value="Search Auction">
</form>
</body>
</html>