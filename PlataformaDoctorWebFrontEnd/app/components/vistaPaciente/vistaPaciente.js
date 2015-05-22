'use strict';

angular.module('mVistaPaciente', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/paciente/:idPaciente/doctor/:idDoctor', {
    templateUrl: 'components/vistaPaciente/vistaPaciente.html',
    controller: 'vistaPacienteCont'
  });
}])

.controller('vistaPacienteCont', ['$scope','$http','$routeParams','$window','md5','$sce',
        function($scope,$http,$routeParams,$window,md5,$sce,ngAudio) {

            console.log("Entro a vista paciente");

        /*
        Se extraen los parametros de la ruta
         */
        $scope.idPaciente=$routeParams.idPaciente;
        $scope.idDoctor=$routeParams.idDoctor;
        $scope.vistaActual=1;
        $scope.states=[];

            $scope.dificultades=[1,2,3,4,5,6,7,8,9,10];
            $scope.localizaciones=["Frontal", "Temporal Izquierdo", "Temporal Derecho" , "Parietal Izquierdo" , "Parietal Derecho" , "Occipital Izquierdo" , "Occipital Derecho"];

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
            $scope.episodios=$scope.paciente.episodios;
            cargarGrafico();
            console.log("Episodios",$scope.episodios);
            $scope.episodioActual=$scope.episodios[0];
            $scope.rutaImagenCerebro ="";
            $scope.cssImagen="";
            cambiarImagen();
            cambiarMedidor(parseInt($scope.episodioActual.nivelDolor));
            if($scope.episodioActual.grabacion!=null) $scope.audio=ngAudio.load($scope.episodioActual.grabacion);
            $scope.nivelDolorNum=parseInt($scope.episodioActual.nivelDolor);
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

        //////////////////////////////////////////////////////////

            function cargarGrafico() {

                $scope.data = [];

                function cargarData() {
                    var cont = 0;
                    for (var i in $scope.episodios) {
                        $scope.data.push({x: i, value: $scope.episodios[i].nivelDolor});
                        cont++;
                    }
                    return cont;
                };

                var numeroTicks = cargarData();

                console.log('Datos de la grafica', $scope.data);

                $scope.options = {
                    axes: {
                        x: {key: 'x', ticksFormat: '.2f', type: 'linear', min: 0, max: 20, ticks: numeroTicks},
                        y: {type: 'linear', min: 0, max: 10, ticks: 10}
                    },
                    margin: {
                        left: 100
                    },
                    series: [
                        {y: 'value', color: 'steelblue', thickness: '2px', type: 'line', striped: true, label: 'Pouet'},
                    ],
                    lineMode: 'linear',
                    tension: 0.7,
                    tooltip: {
                        mode: 'scrubber', formatter: function (x, y, series) {
                            return 'pouet';
                        }
                    },
                    drawLegend: true,
                    drawDots: true,
                    columnsHGap: 5
                };


                $scope.optionsb = {
                    axes: {x: {type: "date", key: "x"}, y: {type: "linear"}},
                    series: [
                        {
                            y: "val_0",
                            label: "A time series",
                            color: "#9467bd",
                            axis: "y",
                            type: "line",
                            thickness: "1px",
                            dotSize: 2,
                            id: "series_0"
                        }
                    ],
                    tooltip: {
                        mode: "scrubber",
                        formatter: function (x, y, series) {
                            return moment(x).fromNow() + ' : ' + y;
                        }
                    },
                    stacks: [],
                    lineMode: "linear",
                    tension: 0.7,
                    drawLegend: true,
                    drawDots: true,
                    columnsHGap: 5
                };

            }



        //////////////////////////////////////////////////////////


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


            /*
            Se piden los doctores para las segundas opiniones
             */
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
                console.log('Success doctores',resp.data);
                crearArreglo($scope.medicos);
            });

            function crearArreglo(arreglo){
                for(var i in arreglo){
                    $scope.states.push({nombre:  arreglo[i].id+'-'+arreglo[i].nombre+' '+arreglo[i].apellido, id: arreglo[i].id});
                }
                console.log('Nuevo arreglo',$scope.states)
            }

            /*
             Para la busqueda dinamica
             */
            $scope.selectors = {};
            $scope.selectors.selected=undefined;
            //$scope.states = ['Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware', 'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky', 'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi', 'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico', 'New York', 'North Dakota', 'North Carolina', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania', 'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont', 'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'];



            /*
            Funcion para pedir segunda opinion
             */
            $scope.pedirSegundaOpinion=function(id,mId){

                var json=[
                    {
                        "idDoctor":mId
                    }
                ];
                var hash=CryptoJS.MD5(JSON.stringify(json));
                var config={headers:{
                    'Content-Type': 'application/json',
                    'X-Auth-Token': $window.sessionStorage.token,
                    'X-Hash': hash,
                    'X-Device': 'WEB'},
                data:json};
                var res =$http.put('https://neuroapi.herokuapp.com/api/paciente/'+$scope.idPaciente+'/episodio/'+id+'/doctores',config);
                res.success(function(data, status, headers, config) {
                    $scope.message = data;
                    //console.log(data);
                }).error(function (data, status, headers, config) {
                    // Erase the token if the user fails to log in
                    delete $window.sessionStorage.token;
                    //console.log('ERROR');
                });

            };


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
                //TODO
                $scope.comentario='';
            }

            /*
            Funcion para buscar por fecha
             */
            $scope.buscarRangoFecha=function(){

                var fecha=$scope.fechas.fechaPicker;

                $scope.fechas.fechaPicker="";

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
                        cambiarMedidor($scope.nivelDolorNum);
                        $scope.recording = $sce.trustAsResourceUrl($scope.episodioActual.grabacion);

                    }
                }
                console.log($scope.episodioActual);
            }

            function cambiarImagen(){
                if($scope.episodioActual.localizacion!=undefined){
               $scope.rutaImagenCerebro = $scope.episodioActual.localizacion;
                }else{
                    $scope.rutaImagenCerebro =="";
                }

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

                $scope.fechas.fechaPicker="";

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
                    $scope.episodios=$scope.paciente.episodios;
                    console.log("Episodios",$scope.episodios);
                    $scope.episodioActual=$scope.episodios[0];
                    $scope.rutaImagenCerebro ="";
                    $scope.cssImagen="";
                    cambiarImagen();
                    cambiarMedidor(parseInt($scope.episodioActual.nivelDolor));
                    if($scope.episodioActual.grabacion!=null) $scope.audio=ngAudio.load($scope.episodioActual.grabacion);
                    $scope.nivelDolorNum=parseInt($scope.episodioActual.nivelDolor);
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

            }


            function buscarMedico(){

            }


            $scope.agregarSegundaOpinion=function(){
                console.log($scope.selectors.selected);


                var pet3={
                    method: 'PUT',
                    url: 'https://neuroapi.herokuapp.com/api/paciente/'+$scope.idPaciente+"/episodio/"+$scope.episodioActual.id+"/doctores",
                    headers:{
                        'X-Auth-Token': $window.sessionStorage.token,
                        'X-Device': 'WEB'
                    },
                    data:{idDoctor:$scope.selectors.selected.split('-')[0]}
                };

                $http(pet3).then(function(resp) {
                    //console.log('Success', resp);
                    console.log('Success Segunda',resp);
                    // For JSON responses, resp.data contains the result
                });

            };

            $scope.cambiarVista=function(vista){
                $scope.vistaActual=vista;
            };


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

