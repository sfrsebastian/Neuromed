'use strict';

angular.module('mInicioDoctor', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/inicioDoctor/:id', {
    templateUrl: 'cInicioDoctor/inicioDoctor.html',
    controller: 'inicioDoctorCont'
  });
}])

.controller('inicioDoctorCont', ['$scope','$window','$http','$routeParams',function($scope,$window,$http,$routeParams) {
        $scope.id=$routeParams.id;
        //console.log('Este es el id del doctor: '+$scope.id);
        //console.log("TOKEN INICIO DOCTOR"+$window.sessionStorage.token);
        var pet1={
            method: 'GET',
            url: 'https://neuromed.herokuapp.com/api/doctor/'+$scope.id,
            headers:{
                'X-Auth-Token': $window.sessionStorage.token
            }

        };

        var pet2={
            method: 'GET',
            url: 'https://neuromed.herokuapp.com/api/doctor/'+$scope.id+'/pacientes',
            headers:{
                'X-Auth-Token': $window.sessionStorage.token
            }

        };

        $http(pet1).then(function(resp) {
            //console.log('Success', resp);
            $scope.medico=resp.data;
            // For JSON responses, resp.data contains the result
        });

        $http(pet2).then(function(resp) {
            //console.log('Success', resp);
            $scope.medicos=resp.data;
        });

       /* $http.get('http://neuromed.herokuapp.com/api/doctor/'+$scope.id).then(function(resp) {
            console.log('Success', resp);
            $scope.medico=resp.data;
            // For JSON responses, resp.data contains the result
        });
        $http.get('http://neuromed.herokuapp.com/api/doctor/'+$scope.id+'/pacientes').then(function(resp) {
            console.log('Success', resp);
            $scope.medicos=resp.data;
            // For JSON responses, resp.data contains the result
        });*/

        $scope.imagenD='http://www.fancyicons.com/free-icons/101/diamond-medical/png/256/patient_256.png';
        $scope.paciente=function(input){
            window.top.location="#/vistaPaciente/"+input+"/"+$scope.id;
            //console.log(input);
        };
}]);