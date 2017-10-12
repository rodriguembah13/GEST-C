(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('EtiquetteSearch', EtiquetteSearch);

    EtiquetteSearch.$inject = ['$resource'];

    function EtiquetteSearch($resource) {
        var resourceUrl =  'api/_search/etiquettes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
