(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('CaisseSearch', CaisseSearch);

    CaisseSearch.$inject = ['$resource'];

    function CaisseSearch($resource) {
        var resourceUrl =  'api/_search/caisses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
