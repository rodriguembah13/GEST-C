(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commande', {
            parent: 'entity',
            url: '/commande?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.commande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commande/commandes.html',
                    controller: 'CommandeController',
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
                    $translatePartialLoader.addPart('commande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('commande-detail', {
            parent: 'commande',
            url: '/commande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.commande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commande/commande-detail.html',
                    controller: 'CommandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Commande', function($stateParams, Commande) {
                    return Commande.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'commande',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('commande-detail.edit', {
            parent: 'commande-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-dialog.html',
                    controller: 'CommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commande', function(Commande) {
                            return Commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commande.new', {
            parent: 'commande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-dialog.html',
                    controller: 'CommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numcommande: null,
                                montanttotalht: null,
                                codebarre: null,
                                libelle: null,
                                montanttotalttc: null,
                                datelimitlivraison: null,
                                datecommande: null,
                                etat: null,
                                soldee: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: 'commande' });
                }, function() {
                    $state.go('commande');
                });
            }]
        })
        .state('commande.edit', {
            parent: 'commande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-dialog.html',
                    controller: 'CommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commande', function(Commande) {
                            return Commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: 'commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commande.delete', {
            parent: 'commande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-delete-dialog.html',
                    controller: 'CommandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Commande', function(Commande) {
                            return Commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: 'commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
