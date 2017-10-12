(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('TypeSortieArticle', TypeSortieArticle);

    TypeSortieArticle.$inject = ['$resource'];

    function TypeSortieArticle ($resource) {
        var resourceUrl =  'api/type-sortie-articles/:id';

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
