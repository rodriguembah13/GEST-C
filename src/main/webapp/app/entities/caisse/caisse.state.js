(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('caisse', {
            parent: 'entity',
            url: '/caisse?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.caisse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/caisse/caisses.html',
                    controller: 'CaisseController',
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
                    $translatePartialLoader.addPart('caisse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('caisse-detail', {
            parent: 'caisse',
            url: '/caisse/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.caisse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/caisse/caisse-detail.html',
                    controller: 'CaisseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('caisse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Caisse', function($stateParams, Caisse) {
                    return Caisse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'caisse',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('caisse-detail.edit', {
            parent: 'caisse-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caisse/caisse-dialog.html',
                    controller: 'CaisseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Caisse', function(Caisse) {
                            return Caisse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('caisse.new', {
            parent: 'caisse',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caisse/caisse-dialog.html',
                    controller: 'CaisseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numcaisse: null,
                                dateOuverture: null,
                                dateFermeture: null,
                                fondcaisse: null,
                                active: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('caisse', null, { reload: 'caisse' });
                }, function() {
                    $state.go('caisse');
                });
            }]
        })
        .state('caisse.edit', {
            parent: 'caisse',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caisse/caisse-dialog.html',
                    controller: 'CaisseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Caisse', function(Caisse) {
                            return Caisse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('caisse', null, { reload: 'caisse' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('caisse.delete', {
            parent: 'caisse',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/caisse/caisse-delete-dialog.html',
                    controller: 'CaisseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Caisse', function(Caisse) {
                            return Caisse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('caisse', null, { reload: 'caisse' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
