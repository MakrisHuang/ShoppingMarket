<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/1/28
  Time: 下午2:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<spring:message code="title.profile.orders" var="ordersTitle"/>
<template:main htmlTitle="${ordersTitle}" bodyTitle="${ordersTitle}">
    <script>
        // perform GET to retrieve orders with AngularJS
        var app = angular.module('orders', []);

        app.controller('OrdersController', ['$http', '$log', function($http, $log){

        }]);
    </script>

</template:main>
