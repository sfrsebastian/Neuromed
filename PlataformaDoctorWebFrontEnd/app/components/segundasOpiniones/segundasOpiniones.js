'use strict';

angular.module('mSegundasOpiniones', ['ngRoute'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/doctor/:idDoctor/opiniones', {
            templateUrl: 'components/segundasOpiniones/segundasOpiniones.html',
            controller: 'segundasOpinionesCont'
        });
    }])

    .controller('segundasOpinionesCont', ['$scope','$http','$routeParams','$window','md5',
        function($scope,$http,$routeParams,$window,md5) {

            console.log("Entro a vista paciente");

            /*
             Se extraen los parametros de la ruta
             */
            $scope.idDoctor=$routeParams.idDoctor;

            $scope.fechas={};


            /*
             Se piden los datos del doctor
             */
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

            /*
             Se piden los datos del paciente
             */
            var pet2={
                method: 'GET',
                url: 'https://neuroapi.herokuapp.com/api/doctor/'+$scope.idDoctor+'/segundaOpinion',
                headers:{
                    'X-Auth-Token': $window.sessionStorage.token,
                    'X-Device': 'WEB'
                }

            };

            $scope.fecha='';
            $scope.comentario='';

            $http(pet2).then(function(resp) {
                //console.log('Success', resp);
                $scope.episodios=resp.data;
                console.log("Episodios",$scope.episodios);
                $scope.episodioActual=$scope.episodios[0];
                $scope.rutaImagenCerebro ="";
                $scope.cssImagen="";
                cambiarImagen();
                cambiarMedidor(parseInt($scope.episodioActual.nivelDolor));
                $scope.nivelDolorNum=parseInt($scope.episodioActual.nivelDolor);

            });






            /*
             Funcion para comentar un episodio
             */
            $scope.comentar = function(id,comentario){

                var json={
                    "contenido":comentario
                };

                var hash=CryptoJS.MD5(JSON.stringify(json));
                var pet4={
                    method: 'POST',
                    url: 'https://neuroapi.herokuapp.com/api/episodio/'+id+'/comentario',
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
                    window.top.location="#"

                }).error(function (data, status, headers, config) {
                    // Erase the token if the user fails to log in
                    delete $window.sessionStorage.token;
                    //console.log('ERROR');
                });

                //console.log($scope.message);
                $scope.comentario='';
            }

            /*
             Funcion para buscar por fecha
             */
            $scope.buscarRangoFecha=function(){

                console.log("Fecha",$scope.fechas.fechaPicker);
                var fecha=$scope.fechas.fechaPicker;

                var fechaI=fecha.split("/")[0];
                var fechaF=fecha.split("/")[1];
                var pet5={
                    method: 'GET',
                    url: 'https://neuroapi.herokuapp.com/api/paciente/'+$scope.idPaciente+'/episodio/'+fechaI+'/'+fechaF,
                    headers:{
                        'X-Auth-Token': $window.sessionStorage.token,
                        'X-Device': 'WEB',
                        'X-Hash': ''
                    }

                };

                $http(pet5).then(function(resp) {
                    $scope.episodios=resp.data;
                    $scope.episodioActual=$scope.episodios[0];
                });

            };

            /*
             Metodo que cambia el paciente actual segun el id que cambia por parametro
             */
            $scope.cambiarEpisodioActual=function(id){
                for(var i = 0; i < $scope.episodios.length; i++)
                {
                    if($scope.episodios[i].id === id)
                    {
                        $scope.episodioActual=$scope.episodios[i];
                        $scope.nivelDolorNum=parseInt($scope.episodioActual.nivelDolor);
                        cambiarImagen();
                        cambiarMedidor(parseInt($scope.episodioActual.nivelDolor));
                    }
                }
                console.log($scope.episodioActual);
            }

            function cambiarImagen(){
                $scope.rutaImagenCerebro = $scope.episodioActual.localizacion;

                if($scope.rutaImagenCerebro=="Frontal"){
                    $scope.cssImagen="brain-frontal";
                }else if($scope.rutaImagenCerebro=="Occipital Derecho"){
                    $scope.cssImagen="brain-occipital-derecho";
                }else if($scope.rutaImagenCerebro=="Occipital Izquierdo"){
                    $scope.cssImagen="brain-occipital-izquierdo";
                }else if($scope.rutaImagenCerebro=="Parietal Derecho"){
                    $scope.cssImagen="brain-parietal-derecho";
                }else if($scope.rutaImagenCerebro=="Parietal Izquierdo"){
                    $scope.cssImagen="brain-parietal-izquierdo";
                }else if($scope.rutaImagenCerebro=="Temporal Izquierdo"){
                    $scope.cssImagen="brain-temp-izquierdo";
                }else if($scope.rutaImagenCerebro=="Temporal Derecho"){
                    $scope.cssImagen="brain-temp-derecho";
                }

            };

            $scope.restaurarEpisodios=function(){


                var pet2={
                    method: 'GET',
                    url: 'https://neuroapi.herokuapp.com/api/doctor/'+$scope.idDoctor+'/segundaOpinion',
                    headers:{
                        'X-Auth-Token': $window.sessionStorage.token,
                        'X-Device': 'WEB'
                    }

                };

                $scope.fecha='';
                $scope.comentario='';

                $http(pet2).then(function(resp) {
                    //console.log('Success', resp);
                    $scope.episodios=resp.data;
                    console.log("Episodios",$scope.episodios);
                    $scope.episodioActual=$scope.episodios[0];
                    $scope.rutaImagenCerebro ="";
                    $scope.cssImagen="";
                    cambiarImagen();
                    cambiarMedidor(parseInt($scope.episodioActual.nivelDolor));
                    $scope.nivelDolorNum=parseInt($scope.episodioActual.nivelDolor);

                });

            }

            $scope.agregarSegundaOpinion=function(){
                console.log($scope.selectors.selected);
            }

            function cambiarMedidor(nivel) {
                console.log('Nivel',nivel);
                var leaseMeter, meterBar, meterBarWidth, meterValue, progressNumber;

                /*Get value of value attribute*/
                var valueGetter = function() {
                    return (nivel/10)*100;
                }

                /*Convert value of value attribute to percentage*/
                var getPercent = function() {
                    meterBarWidth = parseInt(valueGetter());
                    meterBarWidth.toString;
                    meterBarWidth = meterBarWidth + "%";
                    return meterBarWidth;
                }

                /*Apply percentage to width of .meterBar*/
                var adjustWidth = function() {
                    meterBar = document.getElementsByClassName('meterBar');
                    for (var i=0; i<meterBar.length; i++) {
                        var valor = valueGetter();
                        meterBar[i].style['height'] = getPercent();
                        if(valor >= 80){
                            meterBar[i].style['background-color'] = '#DB1212';
                        }
                        else if(valor >= 40){
                            meterBar[i].style['background-color'] = '#FFFF33';
                        }
                        else{
                            meterBar[i].style['background-color'] = '#46DF2E';
                        }
                    }
                }

                /*Update value indicator*/
                var indicUpdate = function() {
                    progressNumber = document.getElementsByClassName('progressNumber');
                    for (var i=0; i<progressNumber.length; i++) {
                        progressNumber[i].innerHTML = valueGetter()/10;
                    }
                }

                adjustWidth();
                indicUpdate();
            };


        }]);

