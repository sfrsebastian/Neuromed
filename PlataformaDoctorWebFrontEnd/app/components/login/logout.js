'use strict';

angular.module('mLogout', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/logout', {
    templateUrl: 'components/login/logout.html',
    controller: 'logoutCont'
  });
}])

.controller('logoutCont', ['$scope','$window','$http' ,function($scope,$window,$http) {

                var pet={
                    method: 'POST',
                    url: 'https://neuroapi.herokuapp.com/api/usuario/logout',
                    headers:{
                        'Content-Type': 'application/json',
                        'X-Auth-Token': $window.sessionStorage.token,
                        'X-Device': 'WEB'
                    }

                };

                $http(pet).success(function(data, status, headers, config) {
                    console.log(status);
                }).error(function (data, status, headers, config) {
                    // Erase the token if the user fails to log in
                    delete $window.sessionStorage.token;

                    // Handle login errors here
                    console.log('ERROR');
                    $scope.message = 'Error: Invalid user or password';
                });

                window.setTimeout(function(){ window.top.location="#/index"},1000)



}]);