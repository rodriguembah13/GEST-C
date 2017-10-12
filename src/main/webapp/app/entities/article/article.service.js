(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Article', Article);

    Article.$inject = ['$resource', 'DateUtils'];

    function Article ($resource, DateUtils) {
        var resourceUrl =  'api/articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datecreation = DateUtils.convertLocalDateFromServer(data.datecreation);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.datecreation = DateUtils.convertLocalDateToServer(copy.datecreation);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.datecreation = DateUtils.convertLocalDateToServer(copy.datecreation);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
