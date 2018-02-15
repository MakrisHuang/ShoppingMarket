angular.module('Store', [])
.controller('CartController', function ($scope, $http, CartHelper) {
    $scope.cartHelper = CartHelper;

    $scope.updateItemInCart = function (item) {
        $scope.cartHelper.updateItemInCart(item);
    };

    $scope.removeItemInCart = function (item) {
        $scope.cartHelper.removeItemInCart(item);
    };
});