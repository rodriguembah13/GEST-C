(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('famille-article', {
            parent: 'entity',
            url: '/famille-article?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.familleArticle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/famille-article/famille-articles.html',
                    controller: 'FamilleArticleController',
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
                    $translatePartialLoader.addPart('familleArticle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('famille-article-detail', {
            parent: 'famille-article',
            url: '/famille-article/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.familleArticle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/famille-article/famille-article-detail.html',
                    controller: 'FamilleArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('familleArticle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FamilleArticle', function($stateParams, FamilleArticle) {
                    return FamilleArticle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'famille-article',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('famille-article-detail.edit', {
            parent: 'famille-article-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille-article/famille-article-dialog.html',
                    controller: 'FamilleArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FamilleArticle', function(FamilleArticle) {
                            return FamilleArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('famille-article.new', {
            parent: 'famille-article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille-article/famille-article-dialog.html',
                    controller: 'FamilleArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                code: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('famille-article', null, { reload: 'famille-article' });
                }, function() {
                    $state.go('famille-article');
                });
            }]
        })
        .state('famille-article.edit', {
            parent: 'famille-article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille-article/famille-article-dialog.html',
                    controller: 'FamilleArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FamilleArticle', function(FamilleArticle) {
                            return FamilleArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('famille-article', null, { reload: 'famille-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('famille-article.delete', {
            parent: 'famille-article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/famille-article/famille-article-delete-dialog.html',
                    controller: 'FamilleArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FamilleArticle', function(FamilleArticle) {
                            return FamilleArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('famille-article', null, { reload: 'famille-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
