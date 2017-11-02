(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('comptoir', {
            parent: 'app',
            url: '/comptoir',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/comptoir/comptoir.html',
                    controller: 'ComptoirController',
                    controllerAs: 'vm'
                }
            }
        }). state('comptoirGros', {
            parent: 'app',
            url: '/comptoirGros',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/comptoir/comptoirGros.html',
                    controller: 'ComptoirController',
                    controllerAs: 'vm'
                }
            }
        })    .state('codebarre', {
            parent: 'app',
            url: '/codebarre',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/comptoir/codebarre.html',
                    controller: 'ComptoirController',
                    controllerAs: 'vm'
                }
            }
        }).state('clientcomptoir.new', {
            parent: 'comptoir',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter:['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client/client-dialog.html',
                    controller: 'ClientDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                civilite: null,
                                reduction: null,
                                telephone: null,
                                email: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('comptoir', null, { reload: 'comptoir' });
                }, function() {
                    $state.go('comptoir');
                });
            }]
        });
    }
})();
