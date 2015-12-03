'use strict';

angular.module('modtools', [
    'config',
    'ngRoute',
    'ngStorage',
   // 'angular-jwt',
    'modtools.controllers',
    'modtools.services'
  ])

  .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when('/', {templateUrl: 'partials/home.html', controller: 'HomeController'});
    $routeProvider.when('/authenticated', {templateUrl: 'partials/authenticated.html', controller: 'AuthController'});
    $routeProvider.otherwise({redirectTo: '/'});

    $locationProvider.html5Mode(true).hashPrefix('!');
  }])

.config(['$httpProvider',
  function ($httpProvider) {
    $httpProvider.interceptors.push(function ($q, $location, $window, $localStorage) {
        return {
            'request': function (config) {
                config.headers = config.headers || {};
                if ($localStorage.id_token) {
                    config.headers.Authorization = 'Bearer ' + $localStorage.id_token;
                } else {
                    $window.location = "/login";
                }
                return config;
            },
            'response': function(response) {
                if ($location.hash()) {
                    $localStorage.id_token = $location.hash().split('=')[2];
                    $location.hash('');
                }
                return response;
            },
            'responseError': function (rejection) {
                if(rejection.status === 401) {
                    $window.location = "/login";
                }
                return $q.reject(rejection);
            }
        };
    });
}]);


