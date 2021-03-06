<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/3/10
  Time: 下午3:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--@elvariable id="orderId" type="java.lang.Integer"--%>
<spring:message code="title.profile.orders.create" var="ordersTitle"/>
<template:main htmlTitle="${ordersTitle}" bodyTitle="${ordersTitle}">
    <section ng-controller="OrderSingleController as singleCtrl">
        <h3 id="orderId" ng-hide="true">${orderId}</h3>
        <h3><spring:message code="orders.title"/>: ${orderId}</h3>
        <table class="table table-striped">
            <thead>
            <tr>
                <th scope="col"><spring:message code="title.profile.orders.create.itemName"/></th>
                <th scope="col"><spring:message code="title.profile.orders.create.price"/></th>
                <th scope="col"><spring:message code="title.profile.orders.create.amount"/></th>
                <th scope="col"><spring:message code="title.profile.orders.create.total"/></th>
            </tr>
            </thead>
            <tbody ng-repeat="item in finishOrder.items">
            <tr>
                <td>{{item.shoppingItem.name}}</td>
                <td>{{item.shoppingItem.price}}</td>
                <td>{{item.amount}}</td>
                <td>{{item.shoppingItem.price * item.amount}}</td>
            </tr>
            </tbody>
            <tfoot>
            <tr>
                <th><spring:message code="title.profile.orders.create.totalPrice"/></th>
                <th></th>
                <th></th>
                <th>{{getOrderTotalPrice()}}</th>
            </tr>
            </tfoot>
        </table>
    </section>
</template:main>