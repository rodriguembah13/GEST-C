(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('EntreeArticle', EntreeArticle)
        .factory('EntreeArticleCom', EntreeArticleCom);

    EntreeArticle.$inject = ['$resource', 'DateUtils'];
    EntreeArticleCom.$inject = ['$resource', 'DateUtils'];
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
    }function EntreeArticleCom ($resource, DateUtils) {
        var resourceUrl =  'api/commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                   // copy.dateentre = DateUtils.convertLocalDateToServer(copy.dateentre);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
