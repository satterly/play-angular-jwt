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
    $routeProvider.otherwise({redirectTo: '/'});

    $locationProvider.html5Mode(true).hashPrefix('!');
  }])

.config(['$httpProvider',
  function ($httpProvider) {
    $httpProvider.interceptors.push(function ($q, $location, $window, $localStorage) {
        return {
            'request': function (config) {
                console.log('request', config.url);
                config.headers = config.headers || {};
                if ($localStorage.id_token) {
                    console.log('request: token=true');
                    config.headers.Authorization = 'Bearer ' + $localStorage.id_token;
                } else {
                    console.log('request: token=false');
                    $window.location = "/login";
                }
                return config;
            },
            'response': function(response) {
                console.log('response', response);
                if ($location.hash()) {
                    console.log('response: hash=true');
                    angular.forEach($location.hash().split('&'), function (hash) {
                        var s = hash.split('=');
                        if (s[0] == 'id_token') {
                            $localStorage.id_token = s[1];
                        }
                    });
                    $location.hash('');
                } else {
                    console.log('response: hash=false');
                }
                return response;
            },
            'responseError': function (rejection) {
                console.log('responseError', rejection);
                if(rejection.status === 401) {
                    console.log('responseError status=401');
                    $window.location = "/login";
                } else {
                    console.log('responseError status!=401');
                }
                return $q.reject(rejection);
            }
        };
    });
}]);


