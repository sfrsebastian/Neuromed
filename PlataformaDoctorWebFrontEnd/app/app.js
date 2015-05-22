'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'mVistaPaciente',
  'mInicioDoctorPacientes','mInicioDoctorAgregar','mLogin','mLogout',
  'myApp.version',
    'mActualizarDatos',
    'angular-md5',
    'mRegistrarDoctor',
    'mExtras','ui.bootstrap','mSegundasOpiniones','ngAudio'


]).
config(['$routeProvider','$locationProvider', function($routeProvider,$locationProvider) {
  $routeProvider
      .otherwise({redirectTo: '/index'});

    //  $locationProvider.html5Mode({
    //    enabled:true,
    //    requiredBase:false
    //  });
}]);
