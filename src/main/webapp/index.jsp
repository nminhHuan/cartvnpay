<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Murach's Java Servlets and JSP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/main.css" type="text/css" />
</head>
<body>
    <h1>CD list</h1>
    <table>
        <tr>
            <th>Description</th>
            <th>Price</th>
            <th>&nbsp;</th>
        </tr>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>${product.description}</td>
                <td>${product.priceCurrencyFormat}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/cart" method="post">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productCode" value="${product.code}">
                        <input type="submit" value="Add To Cart">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
