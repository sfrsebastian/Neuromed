'use strict';

angular.module('mLogin', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/login', {
    templateUrl: 'cLogin/login.html',
    controller: 'loginCont'
  });
}])

.controller('loginCont', ['$scope','$window', function($scope,$window) {
    $scope.entrar=function(){
       // open("#/inicioDoctor");
        window.top.location="#/inicioDoctor";
    };
}]);