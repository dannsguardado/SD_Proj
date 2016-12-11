<%--
  Created by IntelliJ IDEA.
  User: dannsguardado
  Date: 05/12/2016
  Time: 02:13
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>

    <title>Detail Auction</title>

    <link rel="stylesheet" href="css/demo.css">
    <link rel="stylesheet" href="css/header-second-bar.css">

</head>

<body>

<header class="header-two-bars">

    <div class="header-first-bar">

        <div class="header-limiter">

            <h1><a href="#">I<span>bei</span></a></h1>

            <nav>
                <a href="#">My Bids</a>
                <a href="#" class="selected">My Auctions</a>
                <a href="#">My Messages </a>
                <a href="#">cena</a>
            </nav>

            <a href="#" class="logout-button">Logout</a>
        </div>

    </div>

    <div class="header-second-bar">

        <div class="header-limiter">
            <h2><a href="#">Welcome <c:out value="${user.getUser().getName()}"/></a></h2>

            <nav>
                <form action="createpage">
                    <button type="submit">New Auction</button>
                </form>
                <form action="banuserpage">
                    <button type="submit">Ban User</button>
                </form>
                <form action="banuserpage">
                    <button type="submit">Ban User</button>
                </form>
            </nav>

        </div>

    </div>

</header>

<div class="menu">


    <h1>Ibei - the best way to make easy money</h1>


    <form action="detailauction" method="post">
        <input id="ID" type="number" class="form-control" placeholder="ID" name="ID" required/><br>
        <input type="submit" class="btn btn-primary" method="execute" value="Detail Auction">

    </form>

</div>

</body>

</html>
