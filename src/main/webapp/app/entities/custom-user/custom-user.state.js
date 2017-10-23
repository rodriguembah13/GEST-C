(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('custom-user', {
            parent: 'entity',
            url: '/custom-user?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.customUser.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/custom-user/custom-users.html',
                    controller: 'CustomUserController',
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
                    $translatePartialLoader.addPart('customUser');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('custom-user-detail', {
            parent: 'custom-user',
            url: '/custom-user/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.customUser.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/custom-user/custom-user-detail.html',
                    controller: 'CustomUserDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customUser');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomUser', function($stateParams, CustomUser) {
                    return CustomUser.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'custom-user',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('custom-user-detail.edit', {
            parent: 'custom-user-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/custom-user/custom-user-dialog.html',
                    controller: 'CustomUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomUser', function(CustomUser) {
                            return CustomUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('custom-user.new', {
            parent: 'custom-user',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/custom-user/custom-user-dialog.html',
                    controller: 'CustomUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isaduUser: null,
                                isVendre: null,
                                isApprovStock: null,
                                commander: null,
                                isPrintFac: null,
                                isupdateCmde: null,
                                isupdateStck: null,
                                photo: null,
                                photoContentType: null,
                                telephone: null,
                                viewVente: null,
                                viewCmde: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('custom-user', null, { reload: 'custom-user' });
                }, function() {
                    $state.go('custom-user');
                });
            }]
        })
        .state('custom-user.edit', {
            parent: 'custom-user',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/custom-user/custom-user-dialog.html',
                    controller: 'CustomUserDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomUser', function(CustomUser) {
                            return CustomUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('custom-user', null, { reload: 'custom-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('custom-user.delete', {
            parent: 'custom-user',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/custom-user/custom-user-delete-dialog.html',
                    controller: 'CustomUserDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomUser', function(CustomUser) {
                            return CustomUser.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('custom-user', null, { reload: 'custom-user' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
