/**
 * Created by Nicolas on 16/05/15.
 */

angular.module('mExtras', ['ngRoute'])

    .config(['$routeProvider', function($routeProvider) {
        $routeProvider.when('/error', {
            templateUrl: 'components/extras/error.html',
            controller: 'extras'
        });
    }])

    .controller('extras', ['$scope','$window','$http','md5' ,function($scope,$window,$http,md5) {

        $scope.volver = function(){
            window.top.location = "#/login";
        };

    }]);