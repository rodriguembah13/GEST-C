(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('SortieArticleSearch', SortieArticleSearch);

    SortieArticleSearch.$inject = ['$resource'];

    function SortieArticleSearch($resource) {
        var resourceUrl =  'api/_search/sortie-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
