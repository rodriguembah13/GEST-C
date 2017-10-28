(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stat_produit', {
            parent: 'app',
            url: '/stat_produit',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/statistique/stat_produit.html',
                    controller: 'StatistiqueController',
                    controllerAs: 'vm'
                }
            }
        }) .state('stat_vente', {
            parent: 'app',
            url: '/stat_vente',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/statistique/stat_vente.html',
                    controller: 'StatistiqueController',
                    controllerAs: 'vm'
                }
            }
        }) .state('stat_client', {
            parent: 'app',
            url: '/stat_client',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/statistique/stat_client.html',
                    controller: 'StatistiqueController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
