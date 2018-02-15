angular.module('Store', [])
.controller('OrdersController', ['$http', '$log', function($http, $log){
    this.orders = [
        {
            "id": 123,
            "dateCreated": "2016",
            "status": "ready",
            "paymentStatus": "ready",
            "price": 100
        },
        {
            "id": 123,
            "dateCreated": "2016",
            "status": "ready",
            "paymentStatus": "ready",
            "price": 100
        }
    ];


}]);