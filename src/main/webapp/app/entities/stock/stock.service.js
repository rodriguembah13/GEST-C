(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Stock', Stock);

    Stock.$inject = ['$resource', 'DateUtils'];

    function Stock ($resource, DateUtils) {
        var resourceUrl =  'api/stocks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateperemption = DateUtils.convertLocalDateFromServer(data.dateperemption);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateperemption = DateUtils.convertLocalDateToServer(copy.dateperemption);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateperemption = DateUtils.convertLocalDateToServer(copy.dateperemption);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
