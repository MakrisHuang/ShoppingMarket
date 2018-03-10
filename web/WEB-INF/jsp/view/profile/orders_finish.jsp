<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/3/10
  Time: 下午2:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<spring:message code="title.profile.orders.create" var="ordersTitle"/>
<template:main htmlTitle="${ordersTitle}" bodyTitle="${ordersTitle}">
    <section ng-controller="OrdersFinishController">
        <h3>Thanks for Your Ordering!</h3>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th><spring:message code="title.profile.orders.finish.orderId"/></th>
                    <th><spring:message code="title.profile.orders.finish.checkOrder"/></th>
                </tr>
            </thead>
            <tbody>
                    <tr>{{orderId}}</tr>
                    <tr>
                        <button class="btn btn-info float-sm-right" ng-click="checkSpecificOrder(orderId)">
                            <spring:message code="title.profile.orders.finish.checkOrder"/>
                        </button>
                    </tr>
            </tbody>
        </table>
    </section>
</template:main>