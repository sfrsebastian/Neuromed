'use strict';

angular.module('mLogin', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/login', {
    templateUrl: 'cLogin/login.html',
    controller: 'loginCont'
  });
}])

.controller('loginCont', ['$scope','$window','$http','md5' ,function($scope,$window,$http,md5) {


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
            var hash=md5.createHash(JSON.stringify(json));
            var hash64=btoa(hash);
            console.log("Este es el HASH: "+hash);
            console.log("1");
                var pet={
                    method: 'POST',
                    url: 'https://neuroapi.herokuapp.com/api/usuario/autenticar',
                    headers:{
                        'Content-Type': 'application/json',
                        'X-Hash': hash64,
                        'X-Device': 'WEB'
                    },
                    data: json
        //                {
        //                    "email": mail,
        //                    "password": contrasenia
        //              }


                };
            console.log("2");
                $http(pet).success(function(data, status, headers, config) {
                    console.log("STATUS:");
                    console.log(status);

                        $scope.message = data;
                        if (data.rol === "Doctor") {

                            $window.sessionStorage.token = data.token;
                            console.log("TOKEN: " + $window.sessionStorage.token);
                            console.log(data.id);
                            //console.log("ROL: "+data.rol);
                            var id = $scope.message.id;
                            //Hago post
                            if (id != null) {
                                window.top.location = "#/inicioDoctor/" + id;
                            }
                        }else{
                            window.top.location = "#/login/";
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