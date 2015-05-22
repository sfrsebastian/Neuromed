'use strict';

angular.module('mInicioDoctorAgregar', ['ngRoute'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/pacientes/:id', {
            templateUrl: 'components/inicioDoctor/inicioDoctorAgregar.html',
            controller: 'inicioDoctorAgregarCont'
        });
    }])

    .controller('inicioDoctorAgregarCont', ['$scope','$window','$http','$routeParams',function($scope,$window,$http,$routeParams) {

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
            url: 'https://neuroapi.herokuapp.com/api/paciente',
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


        $scope.agregarPaciente=function(id){

            var pet3={
                method: 'PUT',
                url: 'https://neuroapi.herokuapp.com/api/doctor/'+$scope.id+"/pacientes",
                headers:{
                    'X-Auth-Token': $window.sessionStorage.token,
                    'X-Device': 'WEB'
                },
                data:{idPaciente:id}
            };

            $http(pet3).then(function(resp) {
                //console.log('Success', resp);
               console.log('Success',resp);
                // For JSON responses, resp.data contains the result
            });

            for(var i = 0; i < $scope.pacientes.length; i++)
            {
                if($scope.pacientes[i].id === id)
                {
                    //Hacer lo que se deba para agregarlo
                    $scope.pacientes.splice(i);
                    if($scope.pacientes.length) $scope.pacienteActual=$scope.pacientes[0];
                }
            }
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
    }]);/**
 * Created by Nicolas on 18/05/15.
 */
