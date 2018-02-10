angular.module("Store")
.factory("Cart", function CartFactory($http){
    var service = {
        cart: [],
        addToCart: function(shoppingItem){
            return $http.post('/services/Rest/cart')
        }
    };
    
    return service;
});