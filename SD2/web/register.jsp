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

            <div class="buttonLog">
                <form action="login">
                    <button type="submit">Login</button>
                </form>
            </div>
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
        <form action="register" style="text-align:center" class="register-block" method="post">
            <p><strong>Insira o username:</strong></p>
            <input id="username1" type="text" class="form-control" placeholder="Username" name="Username" required/><br>
            <p><strong>Insira a password:</strong></p>
            <input id="password1" type="password" class="form-control" placeholder="Password" name="Password" required/><br>
            <p></p>
            <input type="submit" class="btn btn-primary" method="execute" value="Register">
        </form>
    </div>

</div>

</body>

</html>
