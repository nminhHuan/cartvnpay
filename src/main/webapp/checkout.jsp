<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Checkout - Murach's Java Servlets</title>
    <link rel="stylesheet" href="main.css" type="text/css"/>
</head>
<body>
    <h1>Checkout</h1>

    <c:if test="${not empty error}">
        <p style="color:red"><b>${error}</b></p>
    </c:if>

    <c:choose>
        <c:when test="${empty cart.items}">
            <p>Your cart is empty.</p>
            <form action="cart" method="post">
                <input type="hidden" name="action" value="shop">
                <input type="submit" value="Continue Shopping">
            </form>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Quantity</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Amount</th>
                </tr>
                <c:forEach var="item" items="${cart.items}">
                    <tr>
                        <td>${item.quantity}</td>
                        <td>${item.product.description}</td>
                        <td>${item.product.priceCurrencyFormat}</td>
                        <td>${item.totalCurrencyFormat}</td>
                    </tr>
                </c:forEach>
                <tr>
                    <td colspan="3"><b>Total</b></td>
                    <td><b>${cart.totalCurrencyFormat}</b></td>
                </tr>
            </table>

            <h3>Pay with VNPay</h3>
            <form action="payment" method="post">
                <p>
                    <label for="bankCode">Payment method:</label>
                    <select name="bankCode" id="bankCode">
                        <option value="">VNPay gateway (choose bank on next page)</option>
                        <option value="VNPAYQR">VNPAY QR</option>
                        <option value="VNBANK">Domestic ATM card / account</option>
                        <option value="INTCARD">International card</option>
                    </select>
                </p>
                <input type="submit" value="Pay with VNPay">
            </form>

            <form action="cart" method="post">
                <input type="hidden" name="action" value="cart">
                <input type="submit" value="Back To Cart">
            </form>
        </c:otherwise>
    </c:choose>
</body>
</html>
