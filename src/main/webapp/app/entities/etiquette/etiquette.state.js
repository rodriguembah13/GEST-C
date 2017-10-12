(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('etiquette', {
            parent: 'entity',
            url: '/etiquette?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.etiquette.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/etiquette/etiquettes.html',
                    controller: 'EtiquetteController',
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
                    $translatePartialLoader.addPart('etiquette');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('etiquette-detail', {
            parent: 'etiquette',
            url: '/etiquette/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.etiquette.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/etiquette/etiquette-detail.html',
                    controller: 'EtiquetteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('etiquette');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Etiquette', function($stateParams, Etiquette) {
                    return Etiquette.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'etiquette',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('etiquette-detail.edit', {
            parent: 'etiquette-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etiquette/etiquette-dialog.html',
                    controller: 'EtiquetteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Etiquette', function(Etiquette) {
                            return Etiquette.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('etiquette.new', {
            parent: 'etiquette',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etiquette/etiquette-dialog.html',
                    controller: 'EtiquetteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                etiquette: null,
                                codeBare: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('etiquette', null, { reload: 'etiquette' });
                }, function() {
                    $state.go('etiquette');
                });
            }]
        })
        .state('etiquette.edit', {
            parent: 'etiquette',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etiquette/etiquette-dialog.html',
                    controller: 'EtiquetteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Etiquette', function(Etiquette) {
                            return Etiquette.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('etiquette', null, { reload: 'etiquette' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('etiquette.delete', {
            parent: 'etiquette',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/etiquette/etiquette-delete-dialog.html',
                    controller: 'EtiquetteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Etiquette', function(Etiquette) {
                            return Etiquette.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('etiquette', null, { reload: 'etiquette' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
