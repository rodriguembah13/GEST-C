(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('FormeArticle', FormeArticle);

    FormeArticle.$inject = ['$resource'];

    function FormeArticle ($resource) {
        var resourceUrl =  'api/forme-articles/:id';

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
