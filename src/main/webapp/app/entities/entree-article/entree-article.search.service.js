(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('EntreeArticleSearch', EntreeArticleSearch);

    EntreeArticleSearch.$inject = ['$resource'];

    function EntreeArticleSearch($resource) {
        var resourceUrl =  'api/_search/entree-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
