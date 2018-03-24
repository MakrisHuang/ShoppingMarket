<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true" %>
<%@ attribute name="htmlTitle" type="java.lang.String" rtexprvalue="true"
              required="true" %>
<%@ attribute name="bodyTitle" type="java.lang.String" rtexprvalue="true"
              required="true" %>

<%@ include file="/WEB-INF/jsp/base.jspf" %>
<!DOCTYPE html>
<html ng-app="Store">
    <head>
        <title><spring:message code="title.company"/> - <c:out value="${fn:trim(htmlTitle)}" /></title>

        <!--Bootstrap and jquery-->
        <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css"
              integrity="sha384-Zug+QiDoJOrZ5t4lssLdxGhVrurbmBWopoEl+M6BdEfwnCJZtKxi1KgxUyJq13dy"
              crossorigin="anonymous">

        <!-- icon -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">

        <!-- angularJS -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.6/angular.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.6.6/angular-route.min.js"></script>
        <script src="https://code.angularjs.org/1.6.6/angular-cookies.js"></script>

        <%--Security--%>
        <script src="<c:url value="/resource/js/security/jwt-decode.min.js"/>"></script>
        <script src="<c:url value="/resource/js/security/hmac-sha256.js"/>"></script>
        <script src="http://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/components/enc-base64-min.js"></script>

        <!-- customized -->
        <link rel="stylesheet" href="<c:url value="/resource/stylesheet/dashboard.css" />" />
        <link rel="stylesheet" href="<c:url value="/resource/stylesheet/open-iconic/font/css/open-iconic-bootstrap.css"/>"/>

        <script src="<c:url value="/resource/js/app.js"/>"></script>

        <%--controller--%>
        <script src="<c:url value="/resource/js/controllers/controllers.js"/>"></script>

    </head>
    <body>
        <header ng-controller="AuthController">
            <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
                <a class="navbar-brand" href="/"><spring:message code="title.company"/></a>
                <button class="navbar-toggler d-lg-none" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <%-- collapse navbar--%>
                <div class="collapse navbar-collapse" id="navbarsExampleDefault">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item" ng-hide="authService.isLogin()">
                            <a class="nav-link" href="#" data-toggle="modal" data-target="#loginModal"><spring:message code="title.login"/></a>
                        </li>
                        <li class="nav-item" ng-hide="authService.isLogin()">
                            <a class="nav-link" href="#" data-toggle="modal" data-target="#registerModal"><spring:message code="title.register"/></a>
                        </li>

                        <li class="nav-item" ng-show="authService.isLogin()">
                            <span class="navbar-text"><spring:message code="title.welcome"/> , {{user.username}}</span>
                        </li>
                        <li class="nav-item" ng-show="authService.isLogin()" ng-click="logout()">
                            <a class="nav-link" href=""><spring:message code="nav.item.logout" /></a>
                        </li>
                        <!-- collapse profile -->
                        <li class="nav-item dropdown" ng-show="authService.isLogin()">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                <spring:message code="title.profile" />
                            </a>
                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="#!/profile"><spring:message code="title.profile.basic"/></a>
                                <a class="dropdown-item" href="#!/orders/viewall"><spring:message code="title.profile.orders.viewall" /></a>
                                <a class="dropdown-item" href="#!/cart"><spring:message code="title.profile.cart"/></a>
                            </div>
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
                            <h5 class="modal-title" id="loginModalLabel"><spring:message code="title.login"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
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

                            <form ng-submit="submitLogin()">
                                <label><spring:message code="field.login.username" /></label><br>
                                <input type="text" ng-model="username"><br>
                                <label><spring:message code="field.login.password" /></label><br>
                                <input type="password" ng-model="password"><br><br>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">{{loginTitle}}</button>
                                </div>
                            </form>
                        </div>

                    </div>
                </div>
            </div>

            <!-- Register Modal -->
            <div class="modal fade" id="registerModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="registerModalLabel"><spring:message code="title.register"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <!-- do register jsp fragment -->
                            <form ng-submit="submitRegister()">
                                <label><spring:message code="field.register.username" /></label><br />
                                <input type="text" ng-model="username" /><br />

                                <label><spring:message code="field.register.password" /></label><br />
                                <input type="password" ng-model="password"/><br />

                                <label><spring:message code="field.register.email" /></label><br />
                                <input type="email" ng-model="email"/><br />

                                <label><spring:message code="field.register.telphone" /></label><br />
                                <input type="text" ng-model="telphone"/><br />

                                <label><spring:message code="field.register.address" /></label><br />
                                <input type="text" ng-model="address"/><br /><br />

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">{{registerTitle}}</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <div class="container-fluid">
            <div class="row">
                <nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar"
                     ng-controller="CategoryController">
                    <ul class="nav nav-pills flex-column">
                        <!-- 商品專區 -->
                        <li class="nav-item">
                            <a class="nav-link" href="#homeSubmenu" data-toggle="collapse" aria-expanded="true"><spring:message code="title.shopping"/></a>
                            <ul class="collapse list-unstyled" id="homeSubmenu">
                                <li><a class="nav-link" href="" ng-click="updateContent(0, 'pc', 3)"><spring:message code="title.shopping.pc"/></a></li>
                                <li><a class="nav-link" href="" ng-click="updateContent(0, 'cell phone', 3)"><spring:message code="title.shopping.cellphone"/></a></li>
                                <li><a class="nav-link" href="" ng-click="updateContent(0, 'tv', 3)"><spring:message code="title.shopping.tv"/></a></li>
                            </ul>
                        </li>
                    </ul>
                </nav>

                <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
                    <jsp:doBody/>
                    <div ng-view></div>
                </main>
            </div>
        </div>
    </body>
</html>
