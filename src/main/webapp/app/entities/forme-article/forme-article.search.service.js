(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('FormeArticleSearch', FormeArticleSearch);

    FormeArticleSearch.$inject = ['$resource'];

    function FormeArticleSearch($resource) {
        var resourceUrl =  'api/_search/forme-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
