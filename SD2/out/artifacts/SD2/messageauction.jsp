<%--
  Created by IntelliJ IDEA.
  User: dannsguardado
  Date: 04/12/2016
  Time: 20:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Message Auction</title>
</head>
<body>
<form action="messageauctionpage" method="post">
    <input id="code" type="number" class="form-control" placeholder="Code" name="Code" /><br>
    <input id="message" type="text" class="form-control" placeholder="Message" name="Message" /><br>
    <input type="submit" class="btn btn-primary" method="execute" value="Make message">

</form>
</body>
</html>
