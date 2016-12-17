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
        <p><strong>Data de Criacão:</strong> <c:out value="${auction.getDatacriacaoString()}"/></p>
        <p><strong>Data de Limite:</strong> <c:out value="${auction.getDataLimiteString()}"/></p>
        <p><strong>Ativo:</strong> <c:out value="${auction.getAtivo()}"/></p>
        <p></p>
        <p><strong>Melhor Licitação:</strong></p>
        <p><strong>Username:</strong> <c:out value="${bestbid.getUsername()}"/></p>
        <p><strong>Valor:</strong> <c:out value="${bestbid.getValor()}"/></p>
        <p><strong>Deixe a sua mensagem !</strong></p>
        <form action="messageauctionpage" method="post">
            <p><strong>Insira id do leilao:</strong></p>
            <input id="code" type="number" class="form-control" placeholder="Code" name="Code" /><br>
            <p><strong>Insir a sua mensagem:</strong></p>
            <input id="message" type="text" class="form-control" placeholder="Message" name="Message" /><br>
            <input type="submit" class="btn btn-primary" method="execute" value="Make message">
        </form>
        <c:forEach items="${messages}" var="message">
            <p>--------------------------------------------------------------------</p>
            <p><strong>Mensagem:</strong></p>
            <p><strong>User:</strong> <c:out value="${message.getUsername()}"/></p>
            <p><strong>Message:</strong> <c:out value="${message.getMessage()}"/></p>
        </c:forEach>

    </div>

    <div class="bids">
        <c:forEach items="${bids}" var="bid">
            <p><strong>Licitação:</strong></p>
            <p><strong>User:</strong> <c:out value="${bid.getUsername()}"/></p>
            <p><strong>Valor:</strong> <c:out value="${bid.getValor()}"/></p>
            <p>--------------------------------------------------------------------</p>
        </c:forEach>
        <p><strong>Pretende licitar este leilao ?</strong></p>
        <form action="createbidpage" method="post">
            <p>Insira o id do leilão:</p>
            <input id="code1" type="number" class="form-control" placeholder="ID" name="Code" required/><br>
            <p>Insira o valor da licitação:</p>
            <input id="amount" type="number" class="form-control" placeholder="Amount" name="Amount" required/><br>
            <input type="submit" class="btn btn-primary" method="execute" value="Make Bid">
        </form>

    </div>

</div>

<script src="js/social.js"></script>
<script type="text/javascript">
    window.onload = change_button_on_load(${user.getUser().getIdFacebook()});
</script>
</body>
</html>