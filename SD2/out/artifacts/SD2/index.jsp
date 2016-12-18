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
                <form action="registerpage">
                    <button type="submit">Register</button>
                </form>

            </div>

            <form method="post" action="login">
                <input id="loginType" type="text" value="facebook" name="LoginType" hidden="">
                <button type="submit" method="execute" class="btn btn-block btn-primary btn-facebook">
                    <i class="fa fa-facebook"></i> Login with Facebook
                </button>
            </form>

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


    </div>

</div>


<script src="js/social.js"></script>
<script type="text/javascript">
    window.onload = change_button_on_load(${user.getUser().getIdFacebook()});
</script>
</body>

</html>
