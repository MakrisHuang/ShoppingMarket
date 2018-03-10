<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/3/10
  Time: 下午2:15
  To change this template use File | Settings | File Templates.
--%>
<%--@elvariable id="orderId" type="java.lang.Integer"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<spring:message code="title.profile.orders.create" var="ordersTitle"/>
<template:main htmlTitle="${ordersTitle}" bodyTitle="${ordersTitle}">
    <section ng-controller="OrdersFinishController">
        <h3 id="orderId" ng-hide="true">${orderId}</h3>
        <h3>Thanks for Your Ordering!</h3>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th><spring:message code="title.profile.orders.finish.orderId"/></th>
                    <th><spring:message code="title.profile.orders.finish.checkOrder"/></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>${orderId}</td>
                    <td>
                        <button class="btn btn-info float-sm-left" ng-click="checkOrderContent()">
                            <spring:message code="title.profile.orders.finish.checkOrder"/>
                        </button>
                    </td>
                </tr>
            </tbody>
        </table>
    </section>
</template:main>