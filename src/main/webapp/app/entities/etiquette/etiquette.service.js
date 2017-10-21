(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Etiquette', Etiquette);

    Etiquette.$inject = ['$resource', 'DateUtils'];

    function Etiquette ($resource, DateUtils) {
        var resourceUrl =  'api/etiquettes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateCreation = DateUtils.convertDateTimeFromServer(data.dateCreation);
                        data.dateCrea = DateUtils.convertDateTimeFromServer(data.dateCrea);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
