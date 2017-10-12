(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('FamilleArticleSearch', FamilleArticleSearch);

    FamilleArticleSearch.$inject = ['$resource'];

    function FamilleArticleSearch($resource) {
        var resourceUrl =  'api/_search/famille-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
