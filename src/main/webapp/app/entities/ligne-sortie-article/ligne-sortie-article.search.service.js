(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('LigneSortieArticleSearch', LigneSortieArticleSearch);

    LigneSortieArticleSearch.$inject = ['$resource'];

    function LigneSortieArticleSearch($resource) {
        var resourceUrl =  'api/_search/ligne-sortie-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
