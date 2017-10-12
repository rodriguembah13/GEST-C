(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('DecompositionSearch', DecompositionSearch);

    DecompositionSearch.$inject = ['$resource'];

    function DecompositionSearch($resource) {
        var resourceUrl =  'api/_search/decompositions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
