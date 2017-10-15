(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('mouvement', {
            parent: 'app',
            url: '/mouvement',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                   templateUrl: 'app/mouvement/mouvement.html',
                    controller: 'MouvementController',
                    controllerAs: 'vm'
                }
            }
        }).state('mouv', {
            parent: 'app',
            url: '/mouvementcommande',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/mouvement/mouvementCommande.html',
                    controller: 'MouvementController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
