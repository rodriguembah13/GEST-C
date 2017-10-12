(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-sortie-article', {
            parent: 'entity',
            url: '/type-sortie-article?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.typeSortieArticle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-sortie-article/type-sortie-articles.html',
                    controller: 'TypeSortieArticleController',
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
                    $translatePartialLoader.addPart('typeSortieArticle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-sortie-article-detail', {
            parent: 'type-sortie-article',
            url: '/type-sortie-article/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.typeSortieArticle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-sortie-article/type-sortie-article-detail.html',
                    controller: 'TypeSortieArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeSortieArticle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeSortieArticle', function($stateParams, TypeSortieArticle) {
                    return TypeSortieArticle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'type-sortie-article',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('type-sortie-article-detail.edit', {
            parent: 'type-sortie-article-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-sortie-article/type-sortie-article-dialog.html',
                    controller: 'TypeSortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeSortieArticle', function(TypeSortieArticle) {
                            return TypeSortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-sortie-article.new', {
            parent: 'type-sortie-article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-sortie-article/type-sortie-article-dialog.html',
                    controller: 'TypeSortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-sortie-article', null, { reload: 'type-sortie-article' });
                }, function() {
                    $state.go('type-sortie-article');
                });
            }]
        })
        .state('type-sortie-article.edit', {
            parent: 'type-sortie-article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-sortie-article/type-sortie-article-dialog.html',
                    controller: 'TypeSortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeSortieArticle', function(TypeSortieArticle) {
                            return TypeSortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-sortie-article', null, { reload: 'type-sortie-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-sortie-article.delete', {
            parent: 'type-sortie-article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-sortie-article/type-sortie-article-delete-dialog.html',
                    controller: 'TypeSortieArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeSortieArticle', function(TypeSortieArticle) {
                            return TypeSortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-sortie-article', null, { reload: 'type-sortie-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
