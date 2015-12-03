'use strict';

angular.module('modtools', [
    'config',
    'ngRoute',
    'ngStorage',
    'angular-jwt',
    'modtools.controllers',
    'modtools.services'
  ])

  .config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when('/', {templateUrl: 'partials/home.html', controller: 'HomeController'});
    $routeProvider.when('/authenticated', {templateUrl: 'partials/authenticated.html', controller: 'AuthController'});
    $routeProvider.otherwise({redirectTo: '/'});

    $locationProvider.html5Mode(true).hashPrefix('!');
  }])

//.config(['$httpProvider',
//  function ($httpProvider) {
//    $httpProvider.interceptors.push(function ($q, $location) {
//        return {
//            'request': function (config) {
//                config.headers = config.headers || {};
//                config.headers.Authorization = 'Bearer ' + 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJyZWFkbWUifQ.1c8tkXQFRGpYkRy25jg5Q6xm40CJhSkXreQKEQU4h00';
//                return config;
//            },
//            'response': function (response) {
//                if ($location.hash() != "") {
//                    console.log("Got id_token!!!")
//                    console.log($location.hash());
//                    $location.hash('');
//                }
//                //Will only be called for HTTP up to 300
//                console.log("<300");
//                console.log(response);
//                return response;
//            },
//            'responseError': function (rejection) {
//                if(rejection.status === 401) {
//                    console.log("401 redirect to login");
//                    $location.path('/loginAction');
//                }
//                console.log(rejection);
//                return $q.reject(rejection);
//            }
//        };
//    });
//}]);

//
//.config(function Config($httpProvider, jwtInterceptorProvider) {
//  jwtInterceptorProvider.tokenGetter = function() {
//    return localStorage.getItem('id_token');
//  };
//  $httpProvider.interceptors.push('jwtInterceptor');
//});

.config(['$httpProvider',
  function ($httpProvider) {
    $httpProvider.interceptors.push(function ($q, $location, $window, $localStorage) {
        return {
            'request': function (config) {
                console.log('request');
                config.headers = config.headers || {};
                if ($localStorage.id_token) {
                    config.headers.Authorization = 'Bearer ' + $localStorage.id_token;
                } else if ($location.hash()) {
                    console.log($location.hash);
                    $localStorage.id_token = $location.hash().split('=')[2];
                } else {
                    console.log('window location');
                    $window.location = "/loginAction";
                }
                return config;
            }
        };
    });
}]);


