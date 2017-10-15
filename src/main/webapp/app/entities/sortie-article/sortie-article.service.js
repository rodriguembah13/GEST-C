(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('SortieArticle', SortieArticle);

    SortieArticle.$inject = ['$resource', 'DateUtils'];

    function SortieArticle ($resource, DateUtils) {
        var resourceUrl =  'api/sortie-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datesortie = DateUtils.convertLocalDateFromServer(data.datesortie);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.datesortie = DateUtils.convertLocalDateToServer(copy.datesortie);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.datesortie = DateUtils.convertLocalDateToServer(copy.datesortie);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('SortieArticleU', SortieArticleU);

    SortieArticleU.$inject = ['$resource', 'DateUtils'];

    function SortieArticleU ($resource, DateUtils) {
        var resourceUrl =  'api/sortie-articleuser/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
