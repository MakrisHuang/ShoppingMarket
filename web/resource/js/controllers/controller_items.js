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
.controller('ShoppingItemController', ['CartHelper', '$scope', '$http', function (CartHelper, $scope, $http) {
    $scope.cartHelper = CartHelper;

    $scope.shoppingItems = [

    ];

    $scope.fetchShoppingItems = function (page, category, size) {
        var url = '/services/Rest/shopping?page=' + page
            + "&category=" + category + "&size=" + size;
        $http.get(url).then(function (response) {
            console.log(response);
            $scope.shoppingItems = response.data["content"];
            console.log($scope.shoppingItems);
        }, function(err){
            if (err) {
                console.log("Error fetch shopping items");
            }
        });
    };

    // load default shopping items
    $scope.fetchShoppingItems(0, "pc", 3);

    $scope.addItemToCart = function (item) {
        $scope.cartHelper.addToCart(item);
    };
}])
.controller('CartController', function ($scope, $http, CartHelper) {
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
            $scope.cartHelper.cart.cartItems.forEach(function (item) {
                var count = item.shoppingItem.price * item.amount;
                total = total + count;
            });
        }
        return total;
    }
});