(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('entree-article', {
            parent: 'entity',
            url: '/entree-article?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.entreeArticle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entree-article/entree-articles.html',
                    controller: 'EntreeArticleController',
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
                    $translatePartialLoader.addPart('entreeArticle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('entree-article-detail', {
            parent: 'entree-article',
            url: '/entree-article/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.entreeArticle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entree-article/entree-article-detail.html',
                    controller: 'EntreeArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('entreeArticle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EntreeArticle', function($stateParams, EntreeArticle) {
                    return EntreeArticle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'entree-article',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('entree-article-detail.edit', {
            parent: 'entree-article-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entree-article/entree-article-dialog.html',
                    controller: 'EntreeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntreeArticle', function(EntreeArticle) {
                            return EntreeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entree-article.new', {
            parent: 'entree-article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entree-article/entree-article-dialog.html',
                    controller: 'EntreeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                titre: null,
                                dateentre: null,
                                montant_ht: null,
                                montant_ttc: null,
                                observation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('entree-article', null, { reload: 'entree-article' });
                }, function() {
                    $state.go('entree-article');
                });
            }]
        })
        .state('entree-article.edit', {
            parent: 'entree-article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entree-article/entree-article-dialog.html',
                    controller: 'EntreeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntreeArticle', function(EntreeArticle) {
                            return EntreeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entree-article', null, { reload: 'entree-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entree-article.delete', {
            parent: 'entree-article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entree-article/entree-article-delete-dialog.html',
                    controller: 'EntreeArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EntreeArticle', function(EntreeArticle) {
                            return EntreeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('entree-article', null, { reload: 'entree-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
