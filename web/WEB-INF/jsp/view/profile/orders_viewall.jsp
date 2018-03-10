<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/1/28
  Time: 下午2:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<spring:message code="title.profile.orders.viewall" var="ordersTitle"/>
<template:main htmlTitle="${ordersTitle}" bodyTitle="${ordersTitle}">
    <section ng-controller="OrdersViewallController">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th scope="col"><spring:message code="orders.id"/></th>
                    <th scope="col"><spring:message code="orders.dateCreated"/></th>
                    <th scope="col"><spring:message code="orders.status"/></th>
                    <th scope="col"><spring:message code="orders.paymentStatus"/></th>
                    <th scope="col"><spring:message code="orders.price"/></th>
                    <th scope="col"><spring:message code="orders.askQuestion"/></th>
                </tr>
            </thead>
            <tbody ng-repeat="order in orders">
                <tr>
                    <td>{{order.id}} <a href="/orders/{{order.id}}">(Check it)</a></td>
                    <td>{{order.dateCreated}}</td>
                    <td>{{order.status}}</td>
                    <td>{{order.paymentStatus}}</td>
                    <td>{{order.price}}</td>
                    <td><a href="#"><spring:message code="orders.askQuestion"/></a></td>
                </tr>
            </tbody>
        </table>
    </section>
</template:main>
