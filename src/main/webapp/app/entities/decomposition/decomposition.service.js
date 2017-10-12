(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Decomposition', Decomposition);

    Decomposition.$inject = ['$resource'];

    function Decomposition ($resource) {
        var resourceUrl =  'api/decompositions/:id';

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
