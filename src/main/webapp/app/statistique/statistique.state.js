(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stat_vente', {
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
        });
    }
})();
