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
        $scope.imagenxDefecto='http://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0CAcQjRw&url=http%3A%2F%2Fwww.sojaysalud.com%2Fsoja-en-el-hombre.php&ei=6uAJVeLWLvWNsQTh14CQAQ&bvm=bv.88198703,d.cWc&psig=AFQjCNGBg27nUW51QZ0w5EhnV2LNHvDUog&ust=1426797154526851';
        $scope.paciente=function(input){
            window.top.location="#/vistaPaciente/"+input;
            console.log(input);
        };
}]);