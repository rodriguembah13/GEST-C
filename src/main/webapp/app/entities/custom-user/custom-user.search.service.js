(function() {
    'use strict';

    angular
        .module('gestCApp')
        .factory('CustomUserSearch', CustomUserSearch);

    CustomUserSearch.$inject = ['$resource'];

    function CustomUserSearch($resource) {
        var resourceUrl =  'api/_search/custom-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
