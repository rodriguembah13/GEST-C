(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('magasin', {
            parent: 'entity',
            url: '/magasin?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.magasin.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/magasin/magasins.html',
                    controller: 'MagasinController',
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
                    $translatePartialLoader.addPart('magasin');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('magasin-detail', {
            parent: 'magasin',
            url: '/magasin/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.magasin.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/magasin/magasin-detail.html',
                    controller: 'MagasinDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('magasin');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Magasin', function($stateParams, Magasin) {
                    return Magasin.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'magasin',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('magasin-detail.edit', {
            parent: 'magasin-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magasin/magasin-dialog.html',
                    controller: 'MagasinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Magasin', function(Magasin) {
                            return Magasin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('magasin.new', {
            parent: 'magasin',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magasin/magasin-dialog.html',
                    controller: 'MagasinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                nomMagasin: null,
                                ville: null,
                                telephone: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('magasin', null, { reload: 'magasin' });
                }, function() {
                    $state.go('magasin');
                });
            }]
        })
        .state('magasin.edit', {
            parent: 'magasin',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magasin/magasin-dialog.html',
                    controller: 'MagasinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Magasin', function(Magasin) {
                            return Magasin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('magasin', null, { reload: 'magasin' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('magasin.delete', {
            parent: 'magasin',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/magasin/magasin-delete-dialog.html',
                    controller: 'MagasinDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Magasin', function(Magasin) {
                            return Magasin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('magasin', null, { reload: 'magasin' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
