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
    <div class="header-two-bars">

        <div class="header-first-bar">

            <div class="header-limiter">

                <h1><a href="#">I<span>bei</span></a></h1>

                <nav>
                    <div class="button">
                        <form action="createpage">
                            <button type="submit">Send Message</button>
                        </form>
                        <form action="createpage">
                            <button type="submit">My Messages</button>
                        </form>
                    </div>
                </nav>

                <div class="buttonLog">
                    <form action="logout">
                        <button type="submit">Logout</button>
                    </form>

                    <form id="facebook_form" method="post"> </form>
                </div>
            </div>

        </div>

        <div class="header-second-bar">

            <div class="header-limiter">
                <h2><a href="#">Welcome <c:out value="${user.getUser().getName()}"/></a></h2>

                <div class="button">

                    <form action="searchpage">
                        <button type="submit">Search Auction</button>
                    </form>
                    <form action="detailpage">
                        <button type="submit">Detail Auction</button>
                    </form>

                </div>

            </div>

        </div>

    </div>



</head>

<body>
<div class="menu">


    <h1>Ibei - the best way to make easy money</h1>
    <nav>
        <div class="button">

            <form action="createpage">
                <button type="submit">New Auction</button>
            </form>
            <form action="searchpage">
                <button type="submit">Search Auction</button>
            </form>
            <form action="detailpage">
                <button type="submit">Detail Auction</button>
            </form>
            <form action="myauctions">
                <button type="submit">My Auctions</button>
            </form>
            <form action="createbidpage">
                <button type="submit">Create Bid</button>
            </form>
            <form action="editauctionpage">
                <button type="submit">Edit Auction</button>
            </form>
            <form action="messageauctionpage">
                <button type="submit">Message Auction</button>
            </form>
        </div>
    </nav>

    <div class="info">
        <p>Leilão</p>
        <p><strong>ID:</strong> <c:out value="${auction.getAuctionID()}"/></p>
        <p><strong>Title:</strong> <c:out value="${auction.getTitle()}"/></p>
        <p><strong>Code:</strong> <c:out value="${auction.getCode()}"/></p>
        <p><strong>Owner:</strong> <c:out value="${auction.getName()}"/></p>
        <p><strong>Description:</strong> <c:out value="${auction.getDescription()}"/></p>
        <p><strong>Valor Original:</strong> <c:out value="${auction.getAmount()}"/></p>
        <p><strong>Data de Criacão:</strong> <c:out value="${auction.getDatacriacao()}"/></p>
        <p><strong>Data de Limite:</strong> <c:out value="${auction.getDataLimite()}"/></p>
        <p><strong>Ativo:</strong> <c:out value="${auction.getDescription()}"/></p>
        <p></p>
    </div>

    <div class="bids">

        <form action="editauctionvalue">
            <p><strong>Insira o novo titulo:</strong></p>
            <input id="title" type="text" class="form-control" placeholder="Title" name="Title" required/><br>
            <button type="submit">Edit Title</button>
        </form>
        <form action="editauctionvalue">
            <p><strong>Insira a nova descrição:</strong></p>
            <input id="description" type="text" class="form-control" placeholder="Description" name="Description" required/><br>
            <button type="submit">Edit Description</button>
        </form>
        <form action="editauctionvalue">
            <p><strong>Insira o novo valor:</strong></p>
            <input id="amount" type="text" class="form-control" placeholder="Amount" name="Amount" required/><br>
            <button type="submit">Edit Amount</button>
        </form>
        <form action="editauctionvalue">
            <p><strong>Insira a nova data (YYYY-MM-DD HH:mm):</strong></p>
            <input id="datalimite" type="text" class="form-control" placeholder="Datalimite" name="Datalimite" required/><br>
            <button type="submit">Edit Data Limite</button>
        </form>
    </div>

</div>

<script src="js/social.js"></script>
<script type="text/javascript">
    window.onload = change_button_on_load(${user.getUser().getIdFacebook()});
</script>
</body>
</html>