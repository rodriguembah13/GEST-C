(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Article', Article)
        .factory('GenererCodeArticle', GenererCodeArticle)
        .factory('ImporterArticle', ImporterArticle);

    Article.$inject = ['$resource', 'DateUtils'];
    ImporterArticle.$inject = ['$resource', 'DateUtils'];
    GenererCodeArticle.$inject = ['$resource', 'DateUtils'];
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
        function ImporterArticle ($resource, DateUtils) {
        var resourceUrl =  'api/importerArticleExel/:id';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
                params: {file: null}
            }
        });
    }
    function GenererCodeArticle ($resource, DateUtils) {
        var resourceUrl =  'api/genererCodeArticle/:id';

        return $resource(resourceUrl, {}, {
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    //copy.datecreation = DateUtils.convertLocalDateToServer(copy.datecreation);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
