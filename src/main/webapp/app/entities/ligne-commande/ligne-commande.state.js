(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ligne-commande', {
            parent: 'entity',
            url: '/ligne-commande?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.ligneCommande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-commande/ligne-commandes.html',
                    controller: 'LigneCommandeController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneCommande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ligne-commande-detail', {
            parent: 'ligne-commande',
            url: '/ligne-commande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.ligneCommande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-commande/ligne-commande-detail.html',
                    controller: 'LigneCommandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneCommande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LigneCommande', function($stateParams, LigneCommande) {
                    return LigneCommande.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ligne-commande',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ligne-commande-detail.edit', {
            parent: 'ligne-commande-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-commande/ligne-commande-dialog.html',
                    controller: 'LigneCommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneCommande', function(LigneCommande) {
                            return LigneCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-commande.new', {
            parent: 'ligne-commande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-commande/ligne-commande-dialog.html',
                    controller: 'LigneCommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                designation: null,
                                quantite: null,
                                montanttotalht: null,
                                montanttotalttc: null,
                                prix: null,
                                taxeTva: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ligne-commande', null, { reload: 'ligne-commande' });
                }, function() {
                    $state.go('ligne-commande');
                });
            }]
        })
        .state('ligne-commande.edit', {
            parent: 'ligne-commande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-commande/ligne-commande-dialog.html',
                    controller: 'LigneCommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneCommande', function(LigneCommande) {
                            return LigneCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-commande', null, { reload: 'ligne-commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-commande.delete', {
            parent: 'ligne-commande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-commande/ligne-commande-delete-dialog.html',
                    controller: 'LigneCommandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LigneCommande', function(LigneCommande) {
                            return LigneCommande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-commande', null, { reload: 'ligne-commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
