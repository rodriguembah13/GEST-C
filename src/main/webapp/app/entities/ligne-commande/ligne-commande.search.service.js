(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('LigneCommandeSearch', LigneCommandeSearch);

    LigneCommandeSearch.$inject = ['$resource'];

    function LigneCommandeSearch($resource) {
        var resourceUrl =  'api/_search/ligne-commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
