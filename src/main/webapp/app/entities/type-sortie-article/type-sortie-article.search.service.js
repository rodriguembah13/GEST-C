(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('TypeSortieArticleSearch', TypeSortieArticleSearch);

    TypeSortieArticleSearch.$inject = ['$resource'];

    function TypeSortieArticleSearch($resource) {
        var resourceUrl =  'api/_search/type-sortie-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
