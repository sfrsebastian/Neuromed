'use strict';

angular.module('mRegistrarDoctor', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/registrarse', {
    templateUrl: 'components/registrarDoctor/registrarDoctor.html',
    controller: 'registrarCont'
  });
}])

.controller('registrarCont', ['$scope','$window','$http','md5' ,function($scope,$window,$http,md5) {

            $scope.edit = true;
            $scope.error = false;
            $scope.incomplete = true;

            $scope.nombre='';
            $scope.apellido='';
            $scope.ident='';
            $scope.mail='';
            $scope.fechaNacimiento='';
            $scope.contrasenia='';
            $scope.reptContrasenia='';

            $scope.$watch('nombre',function() {$scope.test();});
            $scope.$watch('apellido',function() {$scope.test();});
            $scope.$watch('ident',function() {$scope.test();});
            $scope.$watch('mail',function() {$scope.test();});
            $scope.$watch('fechaNacimiento',function() {$scope.test();});
            $scope.$watch('contrasenia',function() {$scope.test();});
            $scope.$watch('reptContrasenia',function() {$scope.test();});


            $scope.test = function(){
                if ($scope.contrasenia !== $scope.reptContrasenia) {
                    $scope.error = true;
                } else {
                    $scope.error = false;
                }
                $scope.incomplete = false;
                if ($scope.edit && (!$scope.nombre.length ||
                    !$scope.apellido.length ||
                    !$scope.ident.length || !$scope.mail.length
                    || !$scope.fechaNacimiento.length
                    || !$scope.contrasenia.length || !$scope.reptContrasenia.length)) {
                    $scope.incomplete = true;
                }
            };

            $scope.registrar=function(){

                var json=[
                    {
                        "nombre": $scope.nombre,
                        "apellido": $scope.apellido,
                        "password": $scope.contrasenia,
                        "genero": 1,
                        "identificacion": $scope.ident,
                        "email": $scope.mail,
                        "fechaNacimiento": $scope.fechaNacimiento
                    }
                ];
                console.log(json);
                var hash=CryptoJS.MD5(JSON.stringify(json));;
                var pet={
                    method: 'POST',
                    url: 'http://neuromed.herokuapp.com/api/doctor',
                    headers:{
                        'Content-Type': 'application/json',
                        'X-Auth-Token': $window.sessionStorage.token,
                        'X-Hash': hash64,
                        'X-Device': 'WEB'
                    },
                    data: json
             //       {
             //           "nombre": $scope.nombre,
             //           "apellido": $scope.apellido,
             //           "password": $scope.contrasenia,
             //           "genero": 1,
              //          "identificacion": $scope.ident,
              //          "email": $scope.mail,
              //          "fechaNacimiento": $scope.fechaNacimiento
              //      }


                };
                console.log("2");
                $http(pet).success(function(data, status, headers, config) {
                    $scope.message = data;

                }).error(function (data, status, headers, config) {
                    // Erase the token if the user fails to log in
                    delete $window.sessionStorage.token;

                    // Handle login errors here
                    console.log('ERROR');
                    $scope.message = 'Error: Invalid user or password';
                });

                console.log($scope.message);
                //Hago post
                var usuario=2;//ahi va guardado el post
                if(usuario!=null){
                    window.top.location="#/inicioDoctor";
                }
            }

}]);