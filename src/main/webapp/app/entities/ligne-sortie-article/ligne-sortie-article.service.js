(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('LigneSortieArticle', LigneSortieArticle);

    LigneSortieArticle.$inject = ['$resource'];

    function LigneSortieArticle ($resource) {
        var resourceUrl =  'api/ligne-sortie-articles/:id';

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
