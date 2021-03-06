'use strict';

angular.module('mActualizarDatos', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/actualizarDatos/:id', {
    templateUrl: 'components/actualizarDatos/actualizarDatos.html',
    controller: 'actualizarCont'
  });
}])

.controller('actualizarCont', ['$scope','$window','$http' ,'$routeParams','md5',function($scope,$window,$http,$routeParams,md5) {

        $scope.id=$routeParams.id;


        var pet={
            method: 'GET',
            url: 'https://neuroapi.herokuapp.com/api/doctor/'+$scope.id,
            headers:{
                'X-Auth-Token': $window.sessionStorage.token,
                'X-Device': 'WEB'
            }

        };

        $http(pet).then(function(resp) {
            console.log('Success', resp);
            $scope.medico=resp.data;
            // For JSON responses, resp.data contains the result
        });

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
                    "password": contrasenia,
                    "email": mail
                }
            ];

            var ruta='https://neuroapi.herokuapp.com/api/doctor/'+$scope.id;

            var hash=CryptoJS.MD5(JSON.stringify(json));
            var pet={
                method: 'POST',
                url: ruta,
                headers:{
                    'Content-Type': 'application/json',
                    'X-Auth-Token': $window.sessionStorage.token,
                    'X-Hash': hash,
                    'X-Device': 'WEB'
                },
                data: json


            };
            $http(pet).success(function(data, status, headers, config) {
                $scope.message = data;
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