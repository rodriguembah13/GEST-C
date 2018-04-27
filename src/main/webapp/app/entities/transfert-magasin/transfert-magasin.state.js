(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transfert-magasin', {
            parent: 'entity',
            url: '/transfert-magasin?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.transfertMagasin.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfert-magasin/transfert-magasins.html',
                    controller: 'TransfertMagasinController',
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
                    $translatePartialLoader.addPart('transfertMagasin');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('transfert-magasin-detail', {
            parent: 'transfert-magasin',
            url: '/transfert-magasin/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.transfertMagasin.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfert-magasin/transfert-magasin-detail.html',
                    controller: 'TransfertMagasinDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transfertMagasin');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TransfertMagasin', function($stateParams, TransfertMagasin) {
                    return TransfertMagasin.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transfert-magasin',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transfert-magasin-detail.edit', {
            parent: 'transfert-magasin-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfert-magasin/transfert-magasin-dialog.html',
                    controller: 'TransfertMagasinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransfertMagasin', function(TransfertMagasin) {
                            return TransfertMagasin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfert-magasin.new', {
            parent: 'transfert-magasin',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfert-magasin/transfert-magasin-dialog.html',
                    controller: 'TransfertMagasinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                quantite: null,
                                date_tranfert: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transfert-magasin', null, { reload: 'transfert-magasin' });
                }, function() {
                    $state.go('transfert-magasin');
                });
            }]
        })
        .state('transfert-magasin.edit', {
            parent: 'transfert-magasin',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfert-magasin/transfert-magasin-dialog.html',
                    controller: 'TransfertMagasinDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TransfertMagasin', function(TransfertMagasin) {
                            return TransfertMagasin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfert-magasin', null, { reload: 'transfert-magasin' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfert-magasin.delete', {
            parent: 'transfert-magasin',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfert-magasin/transfert-magasin-delete-dialog.html',
                    controller: 'TransfertMagasinDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TransfertMagasin', function(TransfertMagasin) {
                            return TransfertMagasin.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfert-magasin', null, { reload: 'transfert-magasin' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
