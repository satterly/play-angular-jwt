'use strict';

/* Controllers */

angular.module('modtools.controllers', [])

  .controller('HomeController', ['$scope', '$location', 'Moderation',
    function ($scope, $location, Moderation) {

    Moderation.get(function(response) {
        console.log(response);
        $scope.data = response.data;
        $scope.total = response.total;
    });

  }])

    .controller('AuthController', ['$scope', '$location',
      function ($scope, $location) {

    }]);