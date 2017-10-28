(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('LigneSortieArticle', LigneSortieArticle)
        .factory('LigneSortieStat', LigneSortieStat)
        .factory('LigneSortieStats', LigneSortieStats)
        .factory('LigneSortieStatAchatClient',LigneSortieStatAchatClient)
        .factory('LigneSortieStatyearParam',LigneSortieStatyearParam)
        .factory('LigneSortieStatmonthParam',LigneSortieStatmonthParam)
        .factory('LigneSortieStatmonth',LigneSortieStatmonth);

    LigneSortieArticle.$inject = ['$resource'];
    LigneSortieStat.$inject = ['$resource'];
    LigneSortieStats.$inject = ['$resource'];
    LigneSortieStatAchatClient.$inject = ['$resource'];
    LigneSortieStatyearParam.$inject = ['$resource'];
    LigneSortieStatmonthParam.$inject = ['$resource'];
    LigneSortieStatmonth.$inject = ['$resource'];
    function LigneSortieArticle ($resource) {
        var resourceUrl =  'api/ligne-sortie-articles/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
        function LigneSortieStat ($resource) {
        var service = $resource('api/stat-vente-year-param/:id', {}, {
            'get': {
                method: 'GET',
                isArray: true
            },
            'query': {
                method: 'GET',
                params: {year: null}
            }
        });

        return service;
    }        function LigneSortieStats ($resource) {
        var service = $resource('api/stat-vente-year/:id', {}, {
            'get': {
                method: 'GET',
                isArray: true
            },
            'query': {
                method: 'GET'
            }
        });

        return service;
    }
     function LigneSortieStatAchatClient ($resource) {
        var service = $resource('api/stat-client-vente/:id', {}, {
            'get': {
                method: 'GET',
                isArray: true
            },
            'query': {
                method: 'GET'
            }
        });

        return service;
    }        function LigneSortieStatyearParam ($resource) {
        var service = $resource('api/stat-vente-montant-year-param/:id', {}, {
            'get': {
                method: 'GET',
                isArray: true
            },
            'query': {
                method: 'GET',
                params: {year: null}
            }
        });

        return service;
    } 
     function LigneSortieStatmonthParam ($resource) {
        var service = $resource('api/stat-vente-montant-mois-param/:id', {}, {
            'get': {
                method: 'GET',
                isArray: true
            },
            'query': {
                method: 'GET',
                params: {year: null,monthValue:null}
            }
        });

        return service;
    } 
     function LigneSortieStatmonth ($resource) {
        var service = $resource('api/stat-vente-montant-mois/:id', {}, {
            'get': {
                method: 'GET',
                isArray: true
            },
            'query': {
                method: 'GET'
            }
        });

        return service;
    } 
})();
