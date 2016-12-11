<%@ taglib prefix="s" uri="/struts-tags" %>
<%@  taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ibei</title>
</head>
<body>
<section id="services">
    <div class="container">
        <p><strong>Welcome</strong> <c:out value="${user.getUser().getName()}"/></p>
        <p><strong>Name:</strong> <c:out value="${user.getUser().getName()}"/></p>
        <p><strong>Title:</strong> <c:out value="${auction.getTitle()}"/></p>
        <p><strong>Code:</strong> <c:out value="${auction.getCode()}"/></p>
        <p><strong>Name:</strong> <c:out value="${user.getUser().getName()}"/></p>
        <p><strong>licitacao:</strong> <c:out value="${auction.getAmount()}"/></p>
        <form action="MenuHandling">
            <button type="submit">MENU</button>
        </form>
    </div>
</body>
</html>
