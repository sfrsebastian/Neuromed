'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
  'ngRoute',
  'mVistaPaciente',
  'mInicioDoctorPacientes','mLogin','mLogout',
  'myApp.version',
    'mActualizarDatos',
    'angular-md5',
    'mRegistrarDoctor',
    'mExtras','ui.bootstrap','mSegundasOpiniones','n3-line-chart','mInicioDoctorAgregar'


]).
config(['$routeProvider','$locationProvider', function($routeProvider,$locationProvider) {
  $routeProvider
      .otherwise({redirectTo: '/index'});

    //  $locationProvider.html5Mode({
    //    enabled:true,
    //    requiredBase:false
    //  });
}]);
