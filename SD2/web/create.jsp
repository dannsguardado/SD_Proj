<%--
  Created by IntelliJ IDEA.
  User: ritaalmeida
  Date: 28/11/16
  Time: 03:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Auction</title>
</head>
<body>
    <form action="createauction" method="post">
        <input id="code" type="number" class="form-control" placeholder="Code" name="Code" required/><br>
        <input id="title" type="text" class="form-control" placeholder="Title" name="Title" required/><br>
        <input id="description" type="text" class="form-control" placeholder="Description" name="Description"/><br>
        <input id="amount" type="number" class="form-control" placeholder="Amount" name="Amount" required/><br>
        <input id="datalimite" type="datetime-local" class="form-control" placeholder="DataLimite" name="Datalimite" required/><br>
        <input type="submit" class="btn btn-primary" method="execute" value="Create Auction">
    </form>
</body>
</html>
