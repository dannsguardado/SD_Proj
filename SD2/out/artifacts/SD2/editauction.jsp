<%--
  Created by IntelliJ IDEA.
  User: dannsguardado
  Date: 04/12/2016
  Time: 18:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Auction</title>
</head>
<body>
<form action="editauctionpage" method="post">
    <input id="code" type="number" class="form-control" placeholder="Code" name="Code" required/><br>
    <input id="title" type="text" class="form-control" placeholder="Title" name="Title" /><br>
    <input id="description" type="text" class="form-control" placeholder="Description" name="Description"/><br>
    <input id="amount" type="number" class="form-control" placeholder="Amount" name="Amount" /><br>
    <input id="datalimite" type="datetime-local" class="form-control" placeholder="DataLimite" name="Datalimite" /><br>
    <input type="submit" class="btn btn-primary" method="execute" value="Edit Bid">
</form>
</body>
</html>
