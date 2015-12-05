'use strict';

/* Controllers */

angular.module('modtools.controllers', [])

  .controller('HomeController', ['$scope', '$location', 'Discussion',
    function ($scope, $location, Discussion) {

    Discussion.get({key:'/p/4dgcq'.replace('/p/','')}, function(response) {
        console.log(response);
        $scope.data = response.data;
        $scope.total = response.total;
    });
  }])

    .controller('AntiSpamController', ['$scope', '$location', 'AntiSpam',
      function ($scope, $location, AntiSpam) {

      AntiSpam.list(function(response) {
        $scope.badwords = response.data;
      });

    }]);