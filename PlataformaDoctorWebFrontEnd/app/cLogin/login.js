'use strict';

angular.module('mLogin', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/login', {
    templateUrl: 'cLogin/login.html',
    controller: 'loginCont'
  });
}])

.controller('loginCont', ['$scope','$window','$http' ,function($scope,$window,$http) {

        $scope.edit = true;
        $scope.error = false;
        $scope.incomplete = true;

        $scope.mail='';
        $scope.contrasenia='';

        $scope.$watch('mail',function() {$scope.test();});
        $scope.$watch('contrasenia',function() {$scope.test();});

        $scope.test = function(){

            $scope.incomplete = false;
            if ($scope.edit && (!$scope.mail.length ||
                !$scope.contrasenia.length)) {
                $scope.incomplete = true;
            }
        };

        $scope.update=function(){
                var mail=$scope.mail;
                var contrasenia=$scope.contrasenia;
                var json=[
                    {
                        "email": mail,
                        "password": contrasenia
                    }
                ];
            console.log("1");
                var pet={
                    method: 'POST',
                    url: 'https://neuromed.herokuapp.com/api/usuario/autenticar',
                    headers:{
                        'Content-Type': 'application/json'
                    },
                    data:
                        {
                            "email": mail,
                            "password": contrasenia
                        }


                };
            console.log("2");
                $http(pet).success(function(data, status, headers, config) {
                    $scope.message = data;
                    $window.sessionStorage.token = data.token;
                    console.log("TOKEN: "+$window.sessionStorage.token);
                    console.log(data.id);
                    //console.log("ROL: "+data.rol);
                    var id=$scope.message.id;
                    //Hago post
                    if(id!=null){
                       window.top.location="#/inicioDoctor/"+id;
                    }
                }).error(function (data, status, headers, config) {
                    // Erase the token if the user fails to log in
                    delete $window.sessionStorage.token;

                    // Handle login errors here
                    console.log('ERROR');
                    $scope.message = 'Error: Invalid user or password';
                });


        };
}]);