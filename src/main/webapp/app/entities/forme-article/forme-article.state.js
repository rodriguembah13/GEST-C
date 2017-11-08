(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('forme-article', {
            parent: 'entity',
            url: '/forme-article?page&sort&search',
            data: {
                authorities: ['ROLE_USER','ROLE_CAISSE'],
                pageTitle: 'gestCApp.formeArticle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/forme-article/forme-articles.html',
                    controller: 'FormeArticleController',
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
                    $translatePartialLoader.addPart('formeArticle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('forme-article-detail', {
            parent: 'forme-article',
            url: '/forme-article/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.formeArticle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/forme-article/forme-article-detail.html',
                    controller: 'FormeArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('formeArticle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FormeArticle', function($stateParams, FormeArticle) {
                    return FormeArticle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'forme-article',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('forme-article-detail.edit', {
            parent: 'forme-article-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forme-article/forme-article-dialog.html',
                    controller: 'FormeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormeArticle', function(FormeArticle) {
                            return FormeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('forme-article.new', {
            parent: 'forme-article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forme-article/forme-article-dialog.html',
                    controller: 'FormeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                nomForme: null,
                                quantite: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('forme-article', null, { reload: 'forme-article' });
                }, function() {
                    $state.go('forme-article');
                });
            }]
        })
        .state('forme-article.edit', {
            parent: 'forme-article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forme-article/forme-article-dialog.html',
                    controller: 'FormeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FormeArticle', function(FormeArticle) {
                            return FormeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('forme-article', null, { reload: 'forme-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('forme-article.delete', {
            parent: 'forme-article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/forme-article/forme-article-delete-dialog.html',
                    controller: 'FormeArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FormeArticle', function(FormeArticle) {
                            return FormeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('forme-article', null, { reload: 'forme-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
