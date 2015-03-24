'use strict';

angular.module('mActualizarDatos', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/actualizarDatos/:id', {
    templateUrl: 'cActualizarDatos/actualizarDatos.html',
    controller: 'actualizarCont'
  });
}])

.controller('actualizarCont', ['$scope','$window','$http' ,'$routeParams',function($scope,$window,$http,$routeParams) {

        $scope.id=$routeParams.id;
        $http.get('http://neuromed.herokuapp.com/api/doctor/'+$scope.id).then(function(resp) {
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
            var res =$http.post('http://neuromed.herokuapp.com/api/doctor/'+$scope.id,json);
            res.success(function(data, status, headers, config) {
                $scope.message = data;
                var id=$scope.message.id;
                //Hago post
                if(id!=null){
                    window.top.location="#/inicioDoctor/"+id;
                }
            });


        };

        }]);