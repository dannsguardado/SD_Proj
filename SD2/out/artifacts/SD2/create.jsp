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

    <title>Create Auction</title>

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
<%--
<div id="fb-root"></div>
<script>(function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) return;
    js = d.createElement(s); js.id = id;
    js.src = "//connect.facebook.net/en_GB/sdk.js#xfbml=1&version=v2.8&appId=1427992833880237";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>--%>
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
        <form action="createauction" method="post">
            <p><strong>Insira o codigo do artigo:</strong></p>
            <input id="code" type="number" class="form-control" placeholder="Code" name="Code" required/><br>
            <p><strong>Insira o titulo do leilao:</strong></p>
            <input id="title" type="text" class="form-control" placeholder="Title" name="Title" required/><br>
            <p><strong>Insira a descricao do leilao:</strong></p>
            <input id="description" type="text" class="form-control" placeholder="Description" name="Description"/><br>
            <p><strong>Insira valor do leilao:</strong></p>
            <input id="amount" type="number" class="form-control" placeholder="Amount" name="Amount" required/><br>
            <p><strong>Insira a data de conclusão:</strong></p>
            <input id="datalimite" type="datetime-local" class="form-control" placeholder="DataLimite" name="Datalimite" required/><br>
            <p></p>
            <input type="submit" class="btn btn-primary" method="execute" value="Create Auction">
        </form>

    </div>

</div>

<script src="js/social.js"></script>
<script type="text/javascript">
    window.onload = change_button_on_load(${user.getUser().getIdFacebook()});
</script>

</body>
</html>
