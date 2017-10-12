(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('LigneEntreeArticleSearch', LigneEntreeArticleSearch);

    LigneEntreeArticleSearch.$inject = ['$resource'];

    function LigneEntreeArticleSearch($resource) {
        var resourceUrl =  'api/_search/ligne-entree-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
