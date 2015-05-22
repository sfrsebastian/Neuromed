'use strict';

angular.module('mInicioDoctorPacientes', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/doctor/:id/pacientes', {
    templateUrl: 'components/inicioDoctor/inicioDoctorPacientes.html',
    controller: 'inicioDoctorPacientesCont'
  });
}]).filter('reverse', function() {
        return function(input, uppercase) {
            input = input || '';
            var out = "";
            for (var i = 0; i < input.length; i++) {
                out = input.charAt(i) + out;
            }
            // conditional based on optional argument
            if (uppercase) {
                out = out.toUpperCase();
            }
            return out;
        };
    })

.controller('inicioDoctorPacientesCont', ['$scope','$window','$http','$routeParams',function($scope,$window,$http,$routeParams) {

        $scope.edades=["0-9","10-19","20-29","30-39","40-49","50-59","60-69","70-79","80-89"]

        $scope.id=$routeParams.id;
        var pet1={
            method: 'GET',
            url: 'https://neuroapi.herokuapp.com/api/doctor/'+$scope.id,
            headers:{
                'X-Auth-Token': $window.sessionStorage.token,
                'X-Device': 'WEB'
            }

        };

        var pet2={
            method: 'GET',
            url: 'https://neuroapi.herokuapp.com/api/doctor/'+$scope.id+'/pacientes',
            headers:{
                'X-Auth-Token': $window.sessionStorage.token,
                'X-Device': 'WEB'
            }

        };

        $http(pet1).then(function(resp) {
            //console.log('Success', resp);
            $scope.medico=resp.data;
            // For JSON responses, resp.data contains the result
        });

        $http(pet2).then(function(resp) {
            //console.log('Success', resp);
            $scope.pacientes=resp.data;
            if(resp.data.length){
                $scope.pacienteActual=$scope.pacientes[0];
                console.log("PACIENTE ACTUAL: "+$scope.pacienteActual);
            }
        });


        $scope.imagenD='http://www.fancyicons.com/free-icons/101/diamond-medical/png/256/patient_256.png';

        $scope.paciente=function(input){
            window.top.location="#/paciente/"+input+"/doctor/"+$scope.id;
            //console.log(input);
        };

        $scope.cambiarPacienteActual=function(id){
            for(var i = 0; i < $scope.pacientes.length; i++)
            {
                if($scope.pacientes[i].id === id)
                {
                    $scope.pacienteActual=$scope.pacientes[i];
                }
            }
            console.log($scope.pacienteActual);
        };


}]);