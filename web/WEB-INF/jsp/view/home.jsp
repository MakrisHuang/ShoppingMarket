<%--
  Created by IntelliJ IDEA.
  User: makris
  Date: 2018/1/16
  Time: 上午10:24
  To change this template use File | Settings | File Templates.
--%>

<%--@elvariable id="validationErrors" type="java.util.Set<javax.validation.ConstraintViolation>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<spring:message code="title.home" var="homeTitle" />
<template:main htmlTitle="${homeTitle}" bodyTitle="${homeTitle}">
    <script>
        // perform GET to retrieve shopping items
        // with AngularJS

    </script>
    <section class="row text-center placeholders">
        <div class="col-6 col-sm-3 placeholder">
            <img src="data:image/gif;base64,R0lGODlhAQABAIABAAJ12AAAACwAAAAAAQABAAACAkQBADs=" width="200" height="200" class="img-fluid rounded-circle" alt="Generic placeholder thumbnail">
            <h4>Label</h4>
            <div class="text-muted">Something else</div>
        </div>
        <div class="col-6 col-sm-3 placeholder">
            <img src="data:image/gif;base64,R0lGODlhAQABAIABAADcgwAAACwAAAAAAQABAAACAkQBADs=" width="200" height="200" class="img-fluid rounded-circle" alt="Generic placeholder thumbnail">
            <h4>Label</h4>
            <span class="text-muted">Something else</span>
        </div>
        <div class="col-6 col-sm-3 placeholder">
            <img src="data:image/gif;base64,R0lGODlhAQABAIABAAJ12AAAACwAAAAAAQABAAACAkQBADs=" width="200" height="200" class="img-fluid rounded-circle" alt="Generic placeholder thumbnail">
            <h4>Label</h4>
            <span class="text-muted">Something else</span>
        </div>
        <div class="col-6 col-sm-3 placeholder">
            <img src="data:image/gif;base64,R0lGODlhAQABAIABAADcgwAAACwAAAAAAQABAAACAkQBADs=" width="200" height="200" class="img-fluid rounded-circle" alt="Generic placeholder thumbnail">
            <h4>Label</h4>
            <span class="text-muted">Something else</span>
        </div>
    </section>
</template:main>
