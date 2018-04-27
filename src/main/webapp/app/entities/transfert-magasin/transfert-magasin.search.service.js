(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('TransfertMagasinSearch', TransfertMagasinSearch);

    TransfertMagasinSearch.$inject = ['$resource'];

    function TransfertMagasinSearch($resource) {
        var resourceUrl =  'api/_search/transfert-magasins/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
