<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/2/3
  Time: 下午2:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<spring:message code="title.home" var="homeTitle" />
<%--@elvariable id="cart" type="com.makris.site.entities.Cart" --%>

<template:main htmlTitle="${homeTitle}" bodyTitle="${homeTitle}">
    <section ng-controller="CartController">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th scope="col"><spring:message code="cart.itemName"/></th>
                    <th scope="col"><spring:message code="cart.availableStatus"/></th>
                    <th scope="col"><spring:message code="cart.price"/></th>
                    <th scope="col"><spring:message code="cart.amount"/></th>
                    <th scope="col"><spring:message code="cart.subtotal"/></th>
                    <th scope="col"><spring:message code="cart.action"/></th>
                </tr>
            </thead>
            <tbody ng-repeat="item in cart">
                <tr>
                    <td>{{item.itemName}}</td>
                    <td>{{item.availableStatus}}</td>
                    <td>{{item.price}}</td>
                    <td>
                        <span class="glyphicon glyphicon-plus-sign" ng-click="increaseAmount(orderedItem)"></span>
                            {{item.amount}}
                        <span class="glyphicon glyphicon-minus-sign" ng-click="decreaseAmount(orderedItem, $index)"></span>
                    </td>
                    <td>{{item.price * item.amount}}</td>
                    <td>
                        <button class="btn btn-danger btn-sm" ng-click="removeItemInCart(item)"><spring:message code="cart.cancel"/></button>
                    </td>
                </tr>
            </tbody>
            <caption><spring:message code="cart.totalPrice"/> : {{getCartTotalPrice()}}</caption>
        </table>
    </section>
</template:main>

