<%@ taglib prefix="s" uri="/struts-tags" %>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>

    <title>Second Bar Header</title>

    <link rel="stylesheet" href="css/demo.css">
    <link rel="stylesheet" href="css/header-second-bar.css">

</head>

<body>

<header class="header-two-bars">

    <div class="header-first-bar">

        <div class="header-limiter">

            <h1><a href="#">I<span>bei</span></a></h1>

            <nav>
                <a href="#">MyBids</a>
                <a href="#" class="selected">MyAuctions</a>
                <a href="#">cena</a>
                <a href="#">cena</a>
            </nav>

            <a href="#" class="logout-button">Logout</a>
        </div>

    </div>

    <div class="header-second-bar">

        <div class="header-limiter">
            <h2><a href="#">Hello boss <c:out value="${user.getUser().getName()}"/></a></h2>

        </div>

    </div>

</header>

<div class="menu">


    <h1>Ibei - the best way to make easy money</h1>

    <section id="services">
        <form action="banuserpage">
            <button type="submit">Ban User</button>
        </form>
        <form action="cancelauction">
            <button type="submit">Cancel Auction</button>
        </form>
        <form action="estatisticas">
            <button type="submit">Estatisticas</button>
        </form>

</div>

</body>

</html>
