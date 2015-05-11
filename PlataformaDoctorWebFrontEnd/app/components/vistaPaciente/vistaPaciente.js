'use strict';

angular.module('mVistaPaciente', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/vistaPaciente/:idPaciente/:idDoctor', {
    templateUrl: 'components/vistaPaciente/vistaPaciente.html',
    controller: 'vistaPacienteCont'
  });
}])

.controller('vistaPacienteCont', ['$scope','$http','$routeParams','$window','md5',
        function($scope,$http,$routeParams,$window,md5) {
        $scope.idPaciente=$routeParams.idPaciente;
        $scope.idDoctor=$routeParams.idDoctor;

        var pet1={
            method: 'GET',
            url: 'https://neuroapi.herokuapp.com/api/doctor/'+$scope.idDoctor,
            headers:{
                'X-Auth-Token': $window.sessionStorage.token,
                'X-Device': 'WEB'
            }

        };

        $http(pet1).then(function(resp) {
            //console.log('Success', resp);
            $scope.medico=resp.data;
        });

        var pet2={
            method: 'GET',
            url: 'https://neuroapi.herokuapp.com/api/paciente/'+$scope.idPaciente,
            headers:{
                'X-Auth-Token': $window.sessionStorage.token,
                'X-Device': 'WEB'
            }

        };

        $scope.fecha='';
        $scope.comentario='';

        $http(pet2).then(function(resp) {
            //console.log('Success', resp);
            $scope.paciente=resp.data;
            //console.log("ESTA ES LA INFORMACION DEL PACIENTE: ");
            //console.log($scope.paciente);
            $scope.episodios=$scope.paciente.episodios;
            $scope.info = {
                labels: [],
                nivelDolor:[]
            };

            var datos=$scope.paciente.episodios;
            for(var i in datos)
            {
                $scope.info.labels.push(datos[i].fecha);
            }


            var datos1=$scope.paciente.episodios;
            for(var i in datos1)
            {
                $scope.info.nivelDolor.push(datos[i].nivelDolor);
            }



            // For JSON responses, resp.data contains the result

            $(function () {
                $('#grafico').highcharts({
                    chart: {
                        type: 'line'
                    },
                    title: {
                        text: 'Episodios del paciente'
                    },
                    xAxis: {
                        categories: $scope.info.labels
                    },
                    yAxis: {
                        title: {
                            text: 'Nivel de dolor'
                        }
                    },
                    plotOptions: {
                        line: {
                            dataLabels: {
                                enabled: true
                            },
                            enableMouseTracking: false
                        }
                    },
                    series: [{
                        name: 'Nombre del paciente',
                        data: $scope.info.nivelDolor
                    }]
                });
            });
        });




            $scope.config = {
                title: 'Products',
                tooltips: true,
                labels: false,
                mouseover: function() {},
                mouseout: function() {},
                click: function() {},
                legend: {
                    display: true,
                    //could be 'left, right'
                    position: 'right'
                }
            };


            var pet3={
                method: 'GET',
                url: 'https://neuroapi.herokuapp.com/api/doctor',
                headers:{
                    'X-Auth-Token': $window.sessionStorage.token,
                    'X-Device': 'WEB'
                }

            };

            $http(pet3).then(function(resp) {
                //console.log('Success', resp);
                $scope.medicos=resp.data;
            });



            $scope.pedirSegundaOpinion=function(id,mId){
                //console.log("Este es el id del episodio: "+id);
                //console.log("Este es el id del medico: "+mId);
                var json=[
                    {
                        "idDoctor":mId
                    }
                ];
                var res =$http.put('https://neuroapi.herokuapp.com/api/paciente/'+$scope.idPaciente+'/episodio/'+id+'/doctores',json);
                res.success(function(data, status, headers, config) {
                    $scope.message = data;
                    //console.log(data);
                });
                //console.log($scope.message);

            };

            $scope.comentar = function(id,comentario){
                //console.log("Este es el id: "+id);
                //console.log("Este es el comentario: "+comentario);

                json={
                    "idEpisodio":id,
                    "contenido":comentario
                };

                var hash=CryptoJS.MD5(JSON.stringify(json));;
                var pet4={
                    method: 'POST',
                    url: 'https://neuroapi.herokuapp.com/api/doctor/1/comentario',
                    headers:{
                        'Content-Type': 'application/json',
                        'X-Auth-Token': $window.sessionStorage.token,
                        'X-Hash': hash,
                        'X-Device': 'WEB'
                    },
                    data: json



                };
                $http(pet4).success(function(data, status, headers, config) {
                    $scope.message = data;

                }).error(function (data, status, headers, config) {
                    // Erase the token if the user fails to log in
                    delete $window.sessionStorage.token;
                    //console.log('ERROR');
                });

                //console.log($scope.message);
                $scope.comentario='';
            }

            $scope.buscarRangoFecha=function(){
                window.alert("Funcion en desarrollo"+$scope.fechaI+" "+$scope.fechaF);

                var pet5={
                    method: 'GET',
                    url: 'https://neuroapi.herokuapp.com/api/paciente/'+$scope.idPaciente+'/episodio/'+$scope.fechaI+'/'+$scope.fechaF,
                    headers:{
                        'X-Auth-Token': $window.sessionStorage.token,
                        'X-Device': 'WEB'
                    }

                };

                $http(pet5).then(function(resp) {
                    //console.log('SuccessFecha', resp);
                    $scope.episodiosF=resp.data;
                    $scope.episodios=resp.data;
                });

            };

}]);

