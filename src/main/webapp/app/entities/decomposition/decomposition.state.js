(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('decomposition', {
            parent: 'entity',
            url: '/decomposition?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.decomposition.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/decomposition/decompositions.html',
                    controller: 'DecompositionController',
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
                    $translatePartialLoader.addPart('decomposition');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('decomposition-detail', {
            parent: 'decomposition',
            url: '/decomposition/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.decomposition.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/decomposition/decomposition-detail.html',
                    controller: 'DecompositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('decomposition');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Decomposition', function($stateParams, Decomposition) {
                    return Decomposition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'decomposition',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('decomposition-detail.edit', {
            parent: 'decomposition-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/decomposition/decomposition-dialog.html',
                    controller: 'DecompositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Decomposition', function(Decomposition) {
                            return Decomposition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('decomposition.new', {
            parent: 'decomposition',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/decomposition/decomposition-dialog.html',
                    controller: 'DecompositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                articledepart: null,
                                articlefin: null,
                                quantite: null,
                                etat: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('decomposition', null, { reload: 'decomposition' });
                }, function() {
                    $state.go('decomposition');
                });
            }]
        })
        .state('decomposition.edit', {
            parent: 'decomposition',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/decomposition/decomposition-dialog.html',
                    controller: 'DecompositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Decomposition', function(Decomposition) {
                            return Decomposition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('decomposition', null, { reload: 'decomposition' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('decomposition.delete', {
            parent: 'decomposition',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/decomposition/decomposition-delete-dialog.html',
                    controller: 'DecompositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Decomposition', function(Decomposition) {
                            return Decomposition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('decomposition', null, { reload: 'decomposition' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
