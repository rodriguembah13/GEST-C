(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Fournisseur', Fournisseur);

    Fournisseur.$inject = ['$resource'];

    function Fournisseur ($resource) {
        var resourceUrl =  'api/fournisseurs/:id';

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
