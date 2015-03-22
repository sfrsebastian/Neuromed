'use strict';

angular.module('mInicioDoctor', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/inicioDoctor', {
    templateUrl: 'cInicioDoctor/inicioDoctor.html',
    controller: 'inicioDoctorCont'
  });
}])

.controller('inicioDoctorCont', ['$scope','$http',function($scope,$http) {
        $http.get('http://neuromed.herokuapp.com/api/paciente').then(function(resp) {
            console.log('Success', resp);
            $scope.medicos=resp.data;
            // For JSON responses, resp.data contains the result
        });
        $scope.imagenD='http://www.fancyicons.com/free-icons/101/diamond-medical/png/256/patient_256.png';
        $scope.paciente=function(input){
            window.top.location="#/vistaPaciente/"+input;
            console.log(input);
        };
}]);