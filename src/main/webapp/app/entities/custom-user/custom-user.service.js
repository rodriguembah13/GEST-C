(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('CustomUser', CustomUser);

    CustomUser.$inject = ['$resource'];

    function CustomUser ($resource) {
        var resourceUrl =  'api/custom-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
