<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Payment result - Murach's Java Servlets</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/main.css" type="text/css"/>
</head>
<body>
    <h1>Payment result</h1>

    <p style="color:${success ? 'green' : 'red'}"><b>${message}</b></p>

    <table>
        <tr><td>Order code</td><td>${txnRef}</td></tr>
        <tr><td>Amount</td><td>${amount}</td></tr>
        <tr><td>Bank</td><td>${bankCode}</td></tr>
        <tr><td>VNPay transaction no.</td><td>${transactionNo}</td></tr>
        <tr><td>Paid at</td><td>${payDate}</td></tr>
    </table>

    <c:choose>
        <c:when test="${success}">
            <form action="${pageContext.request.contextPath}/index" method="get">
                <input type="submit" value="Continue Shopping">
            </form>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/cart" method="post">
                <input type="hidden" name="action" value="cart">
                <input type="submit" value="Back To Cart">
            </form>
        </c:otherwise>
    </c:choose>
</body>
</html>
