(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('LigneCommande', LigneCommande);

    LigneCommande.$inject = ['$resource'];

    function LigneCommande ($resource) {
        var resourceUrl =  'api/ligne-commandes/:id';

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
