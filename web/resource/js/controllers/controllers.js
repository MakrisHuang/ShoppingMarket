angular.module('Store', ['ngCookies', 'ngRoute'])
.config(function($routeProvider){
    $routeProvider
        .when('/', {
            templateUrl: 'resource/templates/items.html',
            controller: 'ShoppingItemController'
        })
        .when('/cart', {
            templateUrl: 'resource/templates/cart.html',
            controller: 'CartController'
        })
        .when('/orders/viewall', {
            templateUrl: 'resource/templates/orders_viewall.html',
            controller: 'OrdersViewallController'
        })
        .when('/orders/create', {
            templateUrl: 'resource/templates/orders_create.html',
            controller: 'OrdersCreateController'
        })
        .when('/orders/finish', {
            templateUrl: 'resource/templates/orders_finish.html',
            controller: 'OrdersFinishController'
        })
        .when('/orders/:id', {
            templateUrl: 'resource/templates/orders_single_order.html',
            controller: 'OrdersSingleController'
        })
        .otherwise({ redirectTo: '/'});
})
.factory('AuthService', ['$http', '$cookies', function($http, $cookies){
    return {
        getSecret: function(){
            return $cookies.get('secret');
        },
        setSecret: function(secret){
            $cookies.put('secret', secret);
        },
        removeSecret: function(){
            $cookies.remove('secret');
        },
        isLogin: function(){
            return ($cookies.get('secret') !== undefined) ? true : false;
        },
        setUser: function(user){
            var expireDate = new Date();
            expireDate.setDate(expireDate.getDate() + 1);
            $cookies.putObject('user',user, expireDate);
        },
        removeUser: function(user){
            $cookies.remove('user');
        },
        getUser: function(){
            return $cookies.getObject('user');
        }
    }
}])
.service('JwtHelper', ['AuthService', function(AuthService){
    return {
        // General helper function
        base64url: function(source) {
            // Encode in classical base64
            var encodedSource = CryptoJS.enc.Base64.stringify(source);

            encodedSource = encodedSource.replace(/=+$/, '');
            encodedSource = encodedSource.replace(/\+/g, '-');
            encodedSource = encodedSource.replace(/\//g, '_');
            return encodedSource;
        },
        generateHeader: function(headerMap){
            return this.base64url(CryptoJS.enc.Utf8.parse(JSON.stringify(headerMap)));
        },
        generatePayload: function(payloadMap){
            return this.base64url(CryptoJS.enc.Utf8.parse(JSON.stringify(payloadMap)));;
        },
        generateToken: function(payloadMap){
            var header = {"alg": "HS256", "typ": "JWT"};
            var token = this.generateHeader(header) + "." + this.generatePayload(payloadMap);
            var signature = CryptoJS.HmacSHA256(token, this.getSecret());
            signature = this.base64url(signature);

            var signedToken = token + "." + signature;
            return signedToken;
        },

        decodeToken: function (token) {
            return jwt_decode(token);
        },
        getSecret: function(){
            return AuthService.getSecret();
        }
    };
}])
.factory('CartHelper', ['JwtHelper', 'AuthService', '$http',
    function (JwtHelper, AuthService, $http){
    var cart = {};

    var service = {

        addToCart: function(shoppingItem){
            var jwtEncodedHeader = JwtHelper.generateToken(AuthService.getUser());
            return $http({
                method: 'POST',
                url: 'http://localhost:8080/services/Rest/cart/add',
                headers: {'Content-Type':'application/json',
                          'tokenHeader': jwtEncodedHeader},
                data: shoppingItem
            }).then(
                function (response) {
                    cart = response.data;
                    console.log("[(service) addToCart] cart: ");
                    console.log(cart);

                    alert("商品 " + shoppingItem.name + " 已加入購物車");
                },
                function (err) {
                    console.log("Error in adding shopping item to cart");
                    console.log(err);
                }
            );
        },

        updateItemInCart: function (cartItem) {
            var encodedUser = JwtHelper.generateToken(AuthService.getUser());
            return $http({
                method: 'POST',
                url: 'http://localhost:8080/services/Rest/cart/update',
                headers: {'Content-Type':'application/json',
                          'tokenHeader': encodedUser},
                data: cartItem
            }).then(
                function (response) {
                    cart = response.data;
                    console.log("[(service) updateItemInCart] cart: ");
                    console.log(cart);
                },
                function (err) {
                    console.log("Error in updating shopping item to cart");
                    console.log(err);
                }
            );
        },
        deleteItemInCart: function (shoppingItem) {
            var encodedUser = JwtHelper.generateToken(AuthService.getUser());
            return $http({
                method: 'POST',
                url: 'http://localhost:8080/services/Rest/cart/delete',
                headers: {'Content-Type':'application/json',
                    'tokenHeader': encodedUser},
                data: shoppingItem
            }).then(
                function (response) {
                    cart = response.data;
                    console.log("[(service) deleteItemInCart] cart: ");
                    console.log(cart);
                },
                function (err) {
                    console.log("Error in removing shopping item to cart");
                    console.log(err);
                }
            );
        },
        getCartItems: function(){
            return cart.cartItems;
        },
        setCart: function(newCart){
            cart = newCart;
        },
        getCart: function(){
            return cart;
        }
    };

    return service;
}])
.factory('ShoppingService', ['$http', function($http){
    var shoppingItems = [];

    return {
        fetchShoppingItems: function (page, category, size) {
            var url = '/services/Rest/shopping?page=' + page
                + "&category=" + category + "&size=" + size;
            $http.get(url).then(function (response) {
                shoppingItems = response.data["content"];
                console.log(shoppingItems);

            }, function (err) {
                if (err) {
                    console.log("Error fetch shopping items");
                }
            });
        },
        getItems: function(){
            return shoppingItems;
        }
    }
}])
.controller('AuthController', ['AuthService', '$scope', '$http',
    function(AuthService, $scope, $http){
    $scope.authService = AuthService;
    $scope.user = $scope.authService.getUser();

    $scope.submitLogin = function(){
        if (this.username !== null || this.password !== null){
            var loginInfo = {'username': this.username, 'password': this.password};
            return $http({
                method: 'POST',
                url: 'http://localhost:8080/services/Rest/login',
                data: loginInfo
            }).then(
                function (response){
                    if (response.status === 200){
                        // get secret from header
                        $scope.authService.setSecret(response.headers('secret'));
                        $scope.authService.setUser(response.data);

                        $('#loginModal').modal('hide');
                        $('#navbarsExampleDefault').collapse('hide');
                    }
                },
                function (err){
                    console.log('Error in login with username: ' + $scope.authService.getUser().username);
                }
            )
        }
    };

    $scope.submitRegister = function(){
        var registerInfo = {
            'username': this.username,
            'password': this.password,
            'email': this.email,
            'telphone': this.telphone,
            'address': this.address
        };
        return $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/register',
            data: registerInfo
        }).then(
            function (response){
                if (response.status === 200){
                    $scope.user = response.data;

                    // get secret from header
                    $scope.authService.setSecret(response.headers('secret'));
                    $scope.authService.setUser(response.data);

                    $('#registerModal').modal('hide');
                    $('#navbarsExampleDefault').collapse('hide');
                }
            },
            function (err){
                console.log('Error in login with username: ' + $scope.authService.getUser().username);
            }
        )
    };

    $scope.logout = function(){
        $scope.authService.removeSecret();
        $scope.authService.removeUser();
    }
}])
.controller('CategoryController', ['ShoppingService', '$scope', '$location',
    function(ShoppingService, $scope, $location){
    $scope.shoppingService = ShoppingService;

    $scope.updateContent = function(page, category, size){
        $location.path("/");
        $scope.shoppingService.fetchShoppingItems(page, category, size);
    };

    $scope.shoppingService.fetchShoppingItems(0, 'pc', 3);
}])
.controller('ShoppingItemController', ['CartHelper', 'ShoppingService', '$scope', '$cookies',
    function (CartHelper, ShoppingService, $scope, $cookies) {
    $scope.cartHelper = CartHelper;
    $scope.shoppingService = ShoppingService;

    $scope.addItemToCart = function (item) {
        $scope.cartHelper.addToCart(item);
    };
}])
.controller('CartController', ['JwtHelper', '$scope', '$http', '$window', 'CartHelper',
    'AuthService', '$location', 'OrdersService', function (JwtHelper, $scope, $http, $window, CartHelper,
                                          AuthService, $location, OrdersService) {
    $scope.cartHelper = CartHelper;
    $scope.jwtHelper = JwtHelper;
    $scope.authService = AuthService;

    $scope.fetchCart = function(){
        var encoded = $scope.jwtHelper.generateToken($scope.authService.getUser());
        $http({
            method: 'POST',
            url: "http://localhost:8080/services/Rest/cart/viewall",
            headers: {'Content-Type': 'application/json'},
            data: {"token": encoded}
        }).then(function(response){
            console.log(response.data);
            $scope.cartHelper.setCart(response.data);
        }, function (err) {
            if (err){
                console.log("Error fetching cart");
                console.log(err);
            }
        });
    };

    $scope.fetchCart();

    $scope.increaseAmount = function (cartItem){
        cartItem.amount = cartItem.amount + 1;
        $scope.cartHelper.updateItemInCart(cartItem);
    };

    $scope.decreaseAmount = function (cartItem){
        cartItem.amount = cartItem.amount - 1;
        if (cartItem.amount === 0){
            $scope.cartHelper.deleteItemInCart(cartItem);
            return;
        }
        $scope.cartHelper.updateItemInCart(cartItem);
    };

    $scope.deleteItemInCart = function (shoppingItem) {
        $scope.cartHelper.deleteItemInCart(shoppingItem);
    };

    $scope.getCartTotalPrice = function (){
        var total = 0;
        if ($scope.cartHelper.getCartItems()) {
            angular.forEach($scope.cartHelper.getCartItems(),
            function (value, key) {
                    var count = value.shoppingItem.price * value.amount;
                    total = total + count;
            });
        }
        return total;
    };

    $scope.checkOut = function(cart){
        var cart = {
            "id": cart.id,
            "customerRest": cart.customer,
            "cartItemsRest": cart.cartItems
        };
        console.log("cart in checkOut: ");
        console.log(cart);

        var encodedUser = $scope.jwtHelper.generateToken($scope.authService.getUser());
        $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/orders/create',
            headers: {'Content-Type':'application/json',
                      'tokenHeader': encodedUser},
            data: cart
        }).then(
            function(response){
                console.log(response.data);
                OrdersService.setOrder(response.data);
                $location.path('/orders/create');
            },
            function (err) {
                if (err){
                    console.log("Error in creating new order");
                }
            }
        );
    };
}])
.factory('OrdersService', ['$http', function($http){
    var order = {};
    return {
        getOrder: function(){
            return order;
        },
        setOrder: function(newOrder){
            order = newOrder;
        },
        removeOrder: function(){
            order = {};
        }
    }
}])
.controller('OrdersViewallController', ['$http', '$scope', 'JwtHelper', 'AuthService',
    function($http, $scope, JwtHelper, AuthService){
    $scope.orders = {};

    $scope.fetchAllOrders = function(){
        var encodedUser = JwtHelper.generateToken(AuthService.getUser());
        $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/orders/viewall',
            headers: {'Content-Type':'application/json',
                      'tokenHeader': encodedUser},
            data: null
        }).then(
            function (response) {
                console.log(response);
                $scope.orders = response.data["orders"];
                console.log("[fetchAllOrders] orders: ");
                console.log($scope.orders);
            }, function (err) {
                if (err){
                    console.log("Error fetching all orders: ");
                    console.log(err);
                }
            }
        )
    };

    $scope.fetchAllOrders();

    $scope.checkSpecificOrder = function(orderId){

    }
}])
.controller('OrdersCreateController', ['$http', '$scope', 'OrdersService', 'JwtHelper',
    'AuthService', '$location', function($http, $scope, OrdersService, JwtHelper, AuthService, $location){
    $scope.ordersService = OrdersService;

    $scope.getOrderTotalPrice = function(){
        var totalPrice = 0;
        angular.forEach($scope.ordersService.getOrder().items, function(cartItem, key){
           var count = cartItem.shoppingItem.price * cartItem.amount;
           totalPrice = totalPrice + count;
        });
        return totalPrice;
    };

    $scope.sendOrder = function(){
        var encodedUser = JwtHelper.generateToken(AuthService.getUser());
        $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/orders/finish',
            headers: {'Content-Type':'application/json',
                      'tokenHeader': encodedUser},
            data: $scope.ordersService.getOrder()
        }).then(
            function (response) {
                $scope.ordersService.setOrder(response.data);
                console.log('[Finish order]: ');
                console.log($scope.ordersService.getOrder());

                $location.path('/orders/finish');
            }, function (err) {
                if (err){
                    console.log("Error sending order");
                }
            }
        )
    };
}])
.controller('OrdersFinishController', ['$http', '$scope', '$location', 'OrdersService',
    function($http, $scope, $location, OrdersService){
    $scope.ordersService = OrdersService;

    $scope.checkOrderContent = function(){
        $location.path('/orders/' + $scope.ordersService.getOrder().id);
    }
}])
.controller('OrdersSingleController', ['$http', '$scope', '$routeParams', 'JwtHelper', 'AuthService',
    function($http, $scope, $routeParams, JwtHelper, AuthService){
    $scope.finishOrder = {};

    $scope.fetchSingleOrder = function(){
        var encodedUser = JwtHelper.generateToken(AuthService.getUser());
        $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/orders/' + $routeParams.id,
            headers: {'Content-Type':'application-json',
                      'tokenHeader': encodedUser},
            data: null
        }).then(
            function (response) {
                $scope.finishOrder = response.data;
            }, function (err){
                if (err){
                    console.log("Error fetch single order with orderId: " + $routeParams.id);
                }
            }
        );
    };

    $scope.fetchSingleOrder();
    
    $scope.getOrderTotalPrice = function () {
        var totalPrice = 0;
        angular.forEach($scope.finishOrder.items, function(cartItem, key){
            var count = cartItem.shoppingItem.price * cartItem.amount;
            totalPrice = totalPrice + count;
        });
        return totalPrice;
    }
}]);