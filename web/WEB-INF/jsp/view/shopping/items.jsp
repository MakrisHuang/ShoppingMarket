<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/1/28
  Time: 下午2:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<spring:message code="title.home" var="homeTitle" />
<template:main htmlTitle="${homeTitle}" bodyTitle="${homeTitle}">
    <section class="row text-center placeholders">
        <div class="col-6 col-sm-3 placeholder" ng-repeat="item in shoppingItems">
            <img ng-src="{{'data:image/jpeg;base64,' + item.image}}" width="200" height="200"
                 class="img-fluid" alt="Generic placeholder thumbnail">
            <h4>{{item.name}}, \${{item.price}}</h4>
            <h5><a href="#">{{item.category}}</a></h5>
            <button type="button" class="btn btn-info" ng-click="addItemToCart(item)"
                    data-toggle="popover" title="Add to cart" data-content={{cartHelper.cart}}>Add to Cart</button>
            <div class="text-muted">{{item.description}}</div>
        </div>
    </section>
</template:main>