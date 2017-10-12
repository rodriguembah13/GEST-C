(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('EntreeArticle', EntreeArticle);

    EntreeArticle.$inject = ['$resource', 'DateUtils'];

    function EntreeArticle ($resource, DateUtils) {
        var resourceUrl =  'api/entree-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateentre = DateUtils.convertLocalDateFromServer(data.dateentre);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateentre = DateUtils.convertLocalDateToServer(copy.dateentre);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateentre = DateUtils.convertLocalDateToServer(copy.dateentre);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
