<%--
  Created by IntelliJ IDEA.
  User: ritaalmeida
  Date: 24/11/16
  Time: 19:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>

    <title>Login</title>

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
            <h2><a href="#">Welcome stranger</a></h2>
        </div>

    </div>

</header>

<div class="menu">


    <h1>Ibei - the best way to make easy money</h1>

    <div class="modal-body"><br>
        <form action="login" style="text-align:center" class="login-block" method="post">
            <input id="username" type="text" class="form-control" placeholder="Username" name="Username" required/><br>
            <input id="password" type="password" class="form-control" placeholder="Password" name="Password" required/><br>
            <input type="submit" class="btn btn-primary" method="execute" value="Log in">
        </form>
        <div class="modal-footer">
            <a href="#" data-dismiss="modal" class="btn">Register</a>
        </div>
    </div>

</div>

</body>

</html>
