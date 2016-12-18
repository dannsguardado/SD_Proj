<%--
  Created by IntelliJ IDEA.
  User: dannsguardado
  Date: 04/12/2016
  Time: 18:32
<%--
  Created by IntelliJ IDEA.
  User: ritaalmeida
  Date: 24/11/16
  Time: 19:27
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@	taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>

    <title>Ban User</title>

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

                    <form id="facebook_form" method="post"></form>
                </div>
            </div>

        </div>

        <div class="header-second-bar">

            <div class="header-limiter">
                <h2><a href="#">Welcome <c:out value="${user.getUser().getName()}"/></a></h2>

                <div class="button">

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
        <form action="banuser" method="post">
            <p><strong>Insira o username:</strong></p>
            <input id="username" type="text" class="form-control" placeholder="Username" name="Username" required/><br>
            <p></p>
            <input type="submit" class="btn btn-primary" method="execute" value="Ban User">
        </form>
    </div>

</div>
<script src="js/social.js"></script>
<script type="text/javascript">
    window.onload = change_button_on_load(${user.getUser().getIdFacebook()});
</script>
</body>
</html>