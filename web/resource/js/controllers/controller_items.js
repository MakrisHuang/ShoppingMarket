angular.module('Store', [])
.factory('CartHelper', function ($http){
    var service = {
        cart: [],

        addToCart: function(shoppingItem){
            var param = {
                "action": "add",
                "shoppingItem": shoppingItem
            };
            return $http({
                method: 'POST',
                url: '/services/Rest/cart',
                headers: {'Content-Type': 'application/json', 'charset': 'utf-8'},
                data: $.param(param)}
            ).then(
                function (response) {
                    this.cart = response.data;
                },
                function (err) {
                    console.log("Error in adding shopping item to cart");
                    console.log(err);
                }
            );
        },

        updateItemInCart: function (shoppingItem, newAmount) {
            var param = {
                "action": "update",
                "shoppingItem": shoppingItem,
                "newAmount": newAmount
            };
            return $http.post('/cart', param).then(
                function (response) {
                    this.cart = response.data;
                },
                function (err) {
                    console.log("Error in updating shopping item to cart");
                    console.log(err);
                }
            );
        },

        removeItemInCart: function (shoppingItem) {
            var param = {
                "action": "remove",
                "shoppingItem": shoppingItem
            };
            return $http.post('/cart', param).then(
                function (response) {
                    this.cart = response.data;
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
    $scope.fetchShoppingItems(0, "3c", 3);

    $scope.addItemToCart = function (item) {
        console.log("fire");
        $scope.cartHelper.addToCart(item);
    };
}]);