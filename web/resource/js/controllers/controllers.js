angular.module('Store', [])
.factory('CartHelper', function ($http){
    var service = {
        cart: {},

        addToCart: function(shoppingItem){
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
})
.factory('JwtHelper', function($http){

})
.controller('ShoppingItemController', ['CartHelper', '$scope', '$http', '$location', '$window',
    function (CartHelper, $scope, $http, $location, $window) {
    $scope.cartHelper = CartHelper;

    $scope.shoppingItems = [

    ];

    $scope.updateContent = function(page, category, size){
        console.log("location: " + window.location.pathname);

        if (window.location.pathname !== '/'){
            // then redirect to main page for loading jsp file
            $window.location.href = '/';
        }else{
            $scope.fetchShoppingItems(page, category, size);
        }
    };

    $scope.fetchShoppingItems = function (page, category, size) {
        if (window.location.pathname === '/') {
            var url = '/services/Rest/shopping?page=' + page
                + "&category=" + category + "&size=" + size;
            $http.get(url).then(function (response) {
                $scope.shoppingItems = response.data["content"];
                console.log($scope.shoppingItems);

                window.history.replaceState({}, document.title, "/");
            }, function (err) {
                if (err) {
                    console.log("Error fetch shopping items");
                }
            });
        }
    };

    $scope.fetchShoppingItems(0, 'pc', 3);

    $scope.addItemToCart = function (item) {
        $scope.cartHelper.addToCart(item);
    };
}])
.controller('CartController', function ($scope, $http, $window, CartHelper) {
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
})
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
.controller('OrdersCreateController', ['$http', '$scope', '$window', function($http, $scope, $window){
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
