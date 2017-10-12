(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Magasin', Magasin);

    Magasin.$inject = ['$resource'];

    function Magasin ($resource) {
        var resourceUrl =  'api/magasins/:id';

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
