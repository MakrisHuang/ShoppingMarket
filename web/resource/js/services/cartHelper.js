angular.module('Store', []).factory('CartHelper', function ($scope, $http){
    var service = {
        cart: [],

        addToCart: function(shoppingItem){
            var param = {
                "action": "add",
                "shoppingItem": shoppingItem
            };
            return $http.post('/services/Rest/cart', param).then(
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
});