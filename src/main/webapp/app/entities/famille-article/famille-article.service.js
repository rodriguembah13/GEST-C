(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('FamilleArticle', FamilleArticle);

    FamilleArticle.$inject = ['$resource'];

    function FamilleArticle ($resource) {
        var resourceUrl =  'api/famille-articles/:id';

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
