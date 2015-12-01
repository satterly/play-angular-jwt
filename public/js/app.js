'use strict';

angular.module('modtools', [
    'config',
    'ngRoute',
    'modtools.controllers',
    'modtools.services'
  ])

  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/', {templateUrl: 'partials/home.html', controller: 'HomeController'});
    $routeProvider.when('/authenticated', {templateUrl: 'partials/authenticated.html', controller: 'AuthController'});
    $routeProvider.otherwise({redirectTo: '/'});
  }])

.config(['$httpProvider',
  function ($httpProvider) {
    $httpProvider.interceptors.push(function ($q, $location) {
        return {
            'response': function (response) {
                //Will only be called for HTTP up to 300
                return response;
            },
            'responseError': function (rejection) {
                if(rejection.status === 401) {
                    $location.path('/login');
                }
                return $q.reject(rejection);
            }
        };
    });
}]);