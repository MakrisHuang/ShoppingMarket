var app = angular.module('store-items', []);

app.controller('ShoppingItemController', function ($scope, $http) {
    // $scope.cartHelper = CartHelper;
    //
    // $scope.shoppingItems = [
    //
    // ];
    //
    // $scope.fetchShoppingItems = function (page, category, size) {
    //     var url = '/services/Rest/shopping?page=' + page
    //         + "&category=" + category + "&size=" + size;
    //     $http.get(url).then(function (response) {
    //         console.log(response);
    //         $scope.shoppingItems = response.data["content"];
    //         console.log($scope.shoppingItems);
    //     }, function(err){
    //         if (err) {
    //             console.log("Error fetch shopping items");
    //         }
    //     });
    // };
    //
    // // load default shopping items
    // $scope.fetchShoppingItems(0, "3c", 3);
    //
    // $scope.addItemToCart = function (item) {
    //     $scope.cartHelper.addToCart(item);
    // }
});