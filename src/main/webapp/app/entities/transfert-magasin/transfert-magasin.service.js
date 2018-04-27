(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('TransfertMagasin', TransfertMagasin);

    TransfertMagasin.$inject = ['$resource', 'DateUtils'];

    function TransfertMagasin ($resource, DateUtils) {
        var resourceUrl =  'api/transfert-magasins/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date_tranfert = DateUtils.convertLocalDateFromServer(data.date_tranfert);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_tranfert = DateUtils.convertLocalDateToServer(copy.date_tranfert);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.date_tranfert = DateUtils.convertLocalDateToServer(copy.date_tranfert);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
