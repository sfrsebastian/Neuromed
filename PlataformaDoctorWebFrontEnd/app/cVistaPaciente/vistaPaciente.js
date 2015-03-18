'use strict';

angular.module('mVistaPaciente', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/vistaPaciente/:idPaciente', {
    templateUrl: 'cVistaPaciente/vistaPaciente.html',
    controller: 'vistaPacienteCont'
  });
}])

.controller('vistaPacienteCont', ['$scope','$http','$routeParams',
        function($scope,$http,$routeParams) {
        $scope.idPaciente=$routeParams.idPaciente;
        $http.get('http://neuromed.herokuapp.com/api/paciente').then(function(resp) {
            console.log('Success', resp);
            $scope.medicos=resp.data;
            // For JSON responses, resp.data contains the result
        });
}]);