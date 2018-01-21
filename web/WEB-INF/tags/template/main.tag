<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="htmlTitle" type="java.lang.String" rtexprvalue="true"
              required="true" %>
<%@ attribute name="bodyTitle" type="java.lang.String" rtexprvalue="true"
              required="true" %>
<%@ attribute name="loginFailed" type="java.lang.Boolean" rtexprvalue="true" %>
<%@ attribute name="loginFragment" fragment="true" required="false" %>
<%@ attribute name="registerFragment" fragment="true" required="false" %>
<%@ include file="/WEB-INF/jsp/base.jspf" %>
<!DOCTYPE html>
<html ng-app>
    <head>
        <title><spring:message code="title.customer.support" /> ::
            <c:out value="${fn:trim(htmlTitle)}" /></title>

        <!--Bootstrap and jquery-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css"
              integrity="sha384-Zug+QiDoJOrZ5t4lssLdxGhVrurbmBWopoEl+M6BdEfwnCJZtKxi1KgxUyJq13dy"
              crossorigin="anonymous">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>
        <script src="<c:url value="/resource/js/popper.min.js" />"></script>

        <!-- angularJS -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.6/angular.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.6/angular-route.min.js"></script>

        <!-- customized -->
        <%--<link rel="stylesheet" href="<c:url value="/resource/stylesheet/main.css" />" />--%>
        <link rel="stylesheet" href="<c:url value="/resource/stylesheet/dashboard.css" />" />

        <script type="text/javascript" lang="javascript">
            var postInvisibleForm = function(url, fields) {
                var form = $('<form id="mapForm" method="post"></form>')
                        .attr({ action: url, style: 'display: none;' });
                for(var key in fields) {
                    if(fields.hasOwnProperty(key))
                        form.append($('<input type="hidden">').attr({
                            name: key, value: fields[key]
                        }));
                }
                $('body').append(form);
                form.submit();
            };
            var newChat = function() {
                postInvisibleForm('<c:url value="/chat/new" />', { });
            };
        </script>
    </head>
    <body>
        <header>
            <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
                <a class="navbar-brand" href="#"><spring:message code="title.company"/></a>
                <button class="navbar-toggler d-lg-none" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarsExampleDefault">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item active">
                            <a class="nav-link" href="#">Home <span class="sr-only">(current)</span></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#" data-toggle="modal" data-target="#loginModal"><spring:message code="title.login"/></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#" data-toggle="modal" data-target="#registerModal"><spring:message code="title.register"/></a>
                        </li>
                    </ul>
                    <form class="form-inline mt-2 mt-md-0">
                        <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
                        <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
                    </form>
                </div>
            </nav>

            <!-- Login Modal -->
            <div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="loginModalLabel">Modal title</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <!-- do login jsp fragment -->
                            <spring:message code="message.login.instruction" /><br /><br />
                            <c:if test="${loginFailed}">
                                <b class="errors"><spring:message code="error.login.failed" /></b><br />
                            </c:if>
                            <c:if test="${validationErrors != null}">
                                <div class="errors">
                                    <ul>
                                        <c:forEach items="${validationErrors}" var="error">
                                            <li><c:out value="${error.message}" /></li>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </c:if>

                            <form:form action="home/login" method="post" modelAttribute="loginForm" >
                                <form:label path="username"><spring:message code="field.login.username" /></form:label><br />
                                <form:input path="username" /><br />
                                <form:errors path="username" cssClass="errors" /><br />
                                <form:label path="password"><spring:message code="field.login.password" /></form:label><br />
                                <form:password path="password" /><br />
                                <form:errors path="password" cssClass="errors" /><br />

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary"><spring:message code="field.login.submit" /></button>
                                </div>
                            </form:form>
                        </div>

                    </div>
                </div>
            </div>

            <!-- Register Modal -->
            <div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="registerModalLabel">Modal title</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <!-- do register jsp fragment -->
                            <form:form action="" method="post" modelAttribute="registerForm">
                                <form:label path="username"><spring:message code="field.register.username" /></form:label><br />
                                <form:input path="username" /><br />
                                <form:errors path="username" cssClass="errors" /><br />

                                <form:label path="password"><spring:message code="field.register.password" /></form:label><br />
                                <form:password path="password" /><br />
                                <form:errors path="password" cssClass="errors" /><br />

                                <form:label path="email"><spring:message code="field.register.email" /></form:label><br />
                                <form:input path="email" /><br />
                                <form:errors path="email" cssClass="errors" /><br />

                                <form:label path="telphone"><spring:message code="field.register.telphone" /></form:label><br />
                                <form:input path="telphone" /><br />
                                <form:errors path="telphone" cssClass="errors" /><br />

                                <form:label path="address"><spring:message code="field.register.address" /></form:label><br />
                                <form:input path="address" /><br />
                                <form:errors path="address" cssClass="errors" /><br />

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary"><spring:message code="field.register.submit" /></button>
                                </div>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <div class="container-fluid">
            <div class="row">
                <nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
                    <ul class="nav nav-pills flex-column">
                        <!-- 商品專區 -->
                        <li class="nav-item">
                            <a class="nav-link" href="#homeSubmenu" data-toggle="collapse" aria-expanded="false"><spring:message code="title.shopping"/></a>
                            <ul class="collapse list-unstyled" id="homeSubmenu">
                                <li><a class="nav-link" href="#"><spring:message code="title.shopping.3C"/></a></li>
                                <li><a class="nav-link" href="#"><spring:message code="title.shopping.cellphone"/></a></li>
                                <li><a class="nav-link" href="#"><spring:message code="title.shopping.food"/></a></li>
                            </ul>
                        </li>

                        <!-- 個人專區 -->
                        <li class="nav-item">
                            <a class="nav-link" href="#personMenu" data-toggle="collapse" aria-expanded="false"><spring:message code="title.profile"/></a>
                            <ul class="collapse list-unstyled" id="personMenu">
                                <%--<li><a class="nav-link" href="<c:url value="/ticket/list" />"><spring:message code="nav.item.list.tickets" /></a></li>--%>
                                <%--<li><a class="nav-link" href="<c:url value="/ticket/search" />"><spring:message code="nav.item.search.tickets" /></a></li>--%>
                                <%--<li><a class="nav-link" href="<c:url value="/ticket/create" />"><spring:message code="nav.item.create.ticket" /></a></li>--%>
                                <%--<li><a class="nav-link" href="javascript:void 0;"--%>
                                       <%--onclick="newChat();"><spring:message code="nav.item.chat.support" /></a></li>--%>
                                <%--<li><a class="nav-link" href="<c:url value="/chat/list" />"><spring:message code="nav.item.view.chat" /></a></li>--%>
                                <%--<li><a class="nav-link" href="<c:url value="/session/list" />"><spring:message code="nav.item.list.session" /></a></li>--%>
                                <%--<li><a class="nav-link" href="<c:url value="/profile" />"><spring:message code="nav.item.list.profile" /></a></li>--%>
                                <%--<li><a class="nav-link" href="<c:url value="/logout" />"><spring:message code="nav.item.logout" /></a></li>--%>
                            </ul>
                        </li>
                    </ul>
                </nav>

                <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
                    <!-- do shopping item jsp -->
                    <jsp:doBody/>
                </main>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
                integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
                crossorigin="anonymous"></script>
    </body>
</html>
