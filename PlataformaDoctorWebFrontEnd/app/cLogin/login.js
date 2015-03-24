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
                var res =$http.post('http://neuromed.herokuapp.com/api/usuario/autenticar',json);
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