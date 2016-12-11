<%--
  Created by IntelliJ IDEA.
  User: dannsguardado
  Date: 06/12/2016
  Time: 18:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<section id="services">
    <div class="container">

    <p><strong>Welcome</strong> <c:out value="${user.getUser().getName()}"/></p>

        <p><strong>Top 10 users com mais leiloes criados</strong></p>
        <p><strong>Name:</strong> <c:out value="${userStats1.getUser().getName()}"/></p>
        <p><strong>islive:</strong> <c:out value="${userStats1.getUser().getAuctionsCount()}"/></p>


        <p><strong>top 10 utilizadores que mais leilões venceram</strong></p>
        <p><strong>número total de leilões nos últimos 10 dias</strong></p>

        <form action="MenuHandling">
            <button type="submit">MENU</button>
        </form>
    </div>
</body>
</html>