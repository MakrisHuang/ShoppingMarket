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
        .otherwise({ redirectTo: '/'});
})
.service('JwtHelper', ['$cookies', function($http, $cookies){
    return {
        // General helper function
        generateHeader: function(headerMap){
            var stringifiedHeader = CryptoJS.enc.Utf8.parse(JSON.stringify(headerMap));
            return btoa(stringifiedHeader);
        },
        generatePayload: function(payloadMap){
            var stringifiedHeader = CryptoJS.enc.Utf8.parse(JSON.stringify(payloadMap));
            return btoa(stringifiedHeader);
        },
        generateToken: function(payloadMap){
            var header = {"typ": "JWT", "alg": "HS256"};
            var token = this.generateHeader(header) + "." + this.generatePayload(payloadMap);
            var signature = CryptoJS.HmacSHA256(token, this.getSecret());
            signature = btoa(signature);

            var signedToken = token + "." + signature;
            return signedToken;
        },

        decodeToken: function (token) {
            return jwt_decode(token);
        },
        storeSecret: function(token){
            var decoded = this.decodeToken(token);
            $cookies.put('secret', decoded['secret']);
        },
        getSecret: function(){
            return $cookies.get('secret');
        }
    };
}])
.factory('CartHelper', ['JwtHelper', '$http', function (JwtHelper, $http){
    var service = {
        cart: {},

        addToCart: function(shoppingItem){
            // var encodedShoppingItem = JwtHelper.generateToken(shoppingItem);
            return $http({
                method: 'POST',
                url: 'http://localhost:8080/services/Rest/cart/add',
                headers: {'Content-Type':'application/json'},
                data: shoppingItem
            }).then(
                function (response) {
                    this.cart = response.data;
                    console.log("[(service) addToCart] cart: ");
                    console.log(this.cart);

                    alert("商品 " + shoppingItem.name + " 已加入購物車");
                },
                function (err) {
                    console.log("Error in adding shopping item to cart");
                    console.log(err);
                }
            );
        },

        updateItemInCart: function (cartItem) {
            return $http({
                method: 'POST',
                url: 'http://localhost:8080/services/Rest/cart/update',
                headers: {'Content-Type':'application/json'},
                data: cartItem
            }).then(
                function (response) {
                    this.cart = response.data;
                    console.log("[(service) updateItemInCart] cart: ");
                    console.log(this.cart);
                },
                function (err) {
                    console.log("Error in updating shopping item to cart");
                    console.log(err);
                }
            );
        },

        deleteItemInCart: function (shoppingItem) {
            return $http({
                method: 'POST',
                url: 'http://localhost:8080/services/Rest/cart/delete',
                headers: {'Content-Type':'application/json'},
                data: shoppingItem
            }).then(
                function (response) {
                    this.cart = response.data;
                    console.log("[(service) deleteItemInCart] cart: ");
                    console.log(this.cart);
                },
                function (err) {
                    console.log("Error in removing shopping item to cart");
                    console.log(err);
                }
            );
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
.controller('LoginController', ['$scope', '$http', '$cookies', function($scope, $http, $cookies){
    this.submitLogin = function(){
        if (this.username !== null || this.password !== null){
            var loginInfo = {'username': this.username, 'password': this.password};
            return $http({
                method: 'POST',
                url: 'http://localhost:8080/services/Rest/login',
                data: loginInfo
            }).then(
                function (response){
                    console.log("response: ");
                    console.log(response);

                    $cookies.put('secret', response.data);
                        $('#loginModal').modal('hide');

                },
                function (err){
                    console.log('Error in login with username: ' + this.username);
                }
            )
        }
    }


}])
.controller('CategoryController', ['ShoppingService', '$scope',
    function(ShoppingService, $scope){
    $scope.shoppingService = ShoppingService;

    $scope.updateContent = function(page, category, size){
        $scope.shoppingService.fetchShoppingItems(page, category, size);
    };

    $scope.shoppingService.fetchShoppingItems(0, 'pc', 3);
}])
.controller('ShoppingItemController', ['CartHelper', 'ShoppingService', '$scope',
    function (CartHelper, ShoppingService, $scope) {
    $scope.cartHelper = CartHelper;
    $scope.shoppingService = ShoppingService;

    $scope.addItemToCart = function (item) {
        $scope.cartHelper.addToCart(item);
    };
}])
.controller('CartController', ['$scope', '$http', '$window', 'CartHelper',
    function ($scope, $http, $window, CartHelper) {
    $scope.cartHelper = CartHelper;

    $scope.fetchCart = function(){
        var url = "/services/Rest/cart/view";
        $http.get(url).then(function(response){
            console.log(response.data);
            $scope.cartHelper.cart = response.data;
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
            $scope.cartHelper.removeItemInCart(cartItem);
            return;
        }
        $scope.cartHelper.updateItemInCart(cartItem);
    };

    $scope.deleteItemInCart = function (shoppingItem) {
        $scope.cartHelper.deleteItemInCart(shoppingItem);
    };

    $scope.getCartTotalPrice = function (){
        var total = 0;
        if ($scope.cartHelper.cart) {
            angular.forEach($scope.cartHelper.cart.cartItems,
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

        $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/orders/create',
            headers: {'Content-Type':'application/json'},
            data: cart
        }).then(
            function(response){
                $window.location.href = '/orders?action=create';
            },
            function (err) {
                if (err){
                    console.log("Error in creating new order");
                }
            }
        );
    };
}])
.controller('OrdersViewallController', ['$http', '$scope', '$window',
    function($http, $scope, $window){
    $scope.orders = {};

    $scope.fetchAllOrders = function(){
        $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/orders/viewall',
            headers: {'Content-Type':'application/json'},
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
        $window.location.href = '/orders/' + orderId;
    }

}])
.controller('OrdersCreateController', ['$http', '$scope', '$window',
    function($http, $scope, $window){
    $scope.temporaryOrder = {};

    $scope.fetchTemporaryOrder = function(){
        $http.get("/services/Rest/orders/temporaryOrder").then(
            function (response) {
                $scope.temporaryOrder = response.data;
                console.log("[fetchTempOrder] order: ");
                console.log($scope.temporaryOrder);
            },
            function (err){
                if (err) {
                    console.log("Error fetching temporary order");
                }
            }
        );
    };

    $scope.fetchTemporaryOrder();

    $scope.getOrderTotalPrice = function(){
        var totalPrice = 0;
        angular.forEach($scope.temporaryOrder.items, function(cartItem, key){
           var count = cartItem.shoppingItem.price * cartItem.amount;
           totalPrice = totalPrice + count;
        });
        return totalPrice;
    };

    $scope.sendOrder = function(){
        $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/orders/finish',
            headers: {'Content-Type':'application/json'},
            data: $scope.temporaryOrder
        }).then(
            function (response) {
                // get save order id from server
                var orderId = response.data;
                $window.location.href = "/orders?action=finish&orderId=" + orderId;
            }, function (err) {
                if (err){
                    console.log("Error sending order");
                }
            }
        )
    };
}])
.controller('OrdersFinishController', ['$http', '$scope', '$window',
    function($http, $scope, $window){
    $scope.orderId = document.getElementById("orderId").textContent;

    $scope.checkOrderContent = function(){
        $window.location.href = '/orders/' + $scope.orderId;
    }
}])
.controller('OrderSingleController', ['$http', '$scope', function($http, $scope){
    $scope.orderId = document.getElementById("orderId").textContent;
    
    $scope.finishOrder = {};

    $scope.fetchSingleOrder = function(orderId){
        $http({
            method: 'POST',
            url: 'http://localhost:8080/services/Rest/orders/' + orderId,
            headers: {'Content-Type':'application-json'},
            data: null
        }).then(
            function (response) {
                $scope.finishOrder = response.data;
            }, function (err){
                if (err){
                    console.log("Error fetch single order with orderId: " + orderId);
                }
            }
        );
    };

    $scope.fetchSingleOrder($scope.orderId);
    
    $scope.getOrderTotalPrice = function () {
        var totalPrice = 0;
        angular.forEach($scope.finishOrder.items, function(cartItem, key){
            var count = cartItem.shoppingItem.price * cartItem.amount;
            totalPrice = totalPrice + count;
        });
        return totalPrice;
    }
}]);