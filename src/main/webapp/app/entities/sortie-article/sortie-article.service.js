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
                    /*copy.datesortie = DateUtils.convertLocalDateToServer(copy.datesortie);*/
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
        .factory('SortieArticleU', SortieArticleU)
        .factory('SortieArticleByDate', SortieArticleByDate)
        .factory('SortieArticleByDateUser', SortieArticleByDateUser);

    SortieArticleU.$inject = ['$resource', 'DateUtils'];
    SortieArticleByDate.$inject = ['$resource', 'DateUtils'];
    SortieArticleByDateUser.$inject = ['$resource', 'DateUtils'];
    function SortieArticleU ($resource, DateUtils) {
        var resourceUrl =  'api/sortie-articleuser/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
        function SortieArticleByDate ($resource, DateUtils) {
        var resourceUrl =  'api/sortie-articles-bydate/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true,
                        params: {dateDebut: null,dateDebut: null}}
        });
    }
            function SortieArticleByDateUser ($resource, DateUtils) {
        var resourceUrl =  'api/sortie-articles-bydate-user/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true,
                        params: {dateDebut: null,dateDebut: null}}
        });
    }
})();
(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('SortieArticlePrint', SortieArticlePrint);

    SortieArticlePrint.$inject = ['$resource', 'DateUtils'];

    function SortieArticlePrint ($resource, DateUtils) {
        var resourceUrl =  'api/PrintFacture/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',responseType:'arraybuffer'
              }
        });
    }
})();

