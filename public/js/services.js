'use strict';

/* Services */

angular.module('modtools.services', ['config', 'ngResource'])

  .factory('Moderation', ['$resource', 'config', function ($resource, config) {
    return $resource('/json', {}, {
      'get':   {method:'GET', url:'/json'}
    })
  }])

    .factory('Discussion', ['$resource', 'config', function ($resource, config) {
      return $resource('/json', {}, {
        'get':   {method:'GET', url:'/api/discussions//p/:key'}
      })
    }])

    .factory('AntiSpam', ['$resource', 'config', function ($resource, config) {
      return $resource('/json', {}, {
        'list':   {method:'GET', url:'/api/antispam'},
        'add':    {method:'POST', url:'/api/antispam/:word'},
        'delete': {method:'DELETE', url:'/api/antispam/:word'}
      })
    }]);
