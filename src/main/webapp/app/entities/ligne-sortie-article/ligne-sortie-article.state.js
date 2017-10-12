(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ligne-sortie-article', {
            parent: 'entity',
            url: '/ligne-sortie-article?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.ligneSortieArticle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-sortie-article/ligne-sortie-articles.html',
                    controller: 'LigneSortieArticleController',
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
                    $translatePartialLoader.addPart('ligneSortieArticle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ligne-sortie-article-detail', {
            parent: 'ligne-sortie-article',
            url: '/ligne-sortie-article/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.ligneSortieArticle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-sortie-article/ligne-sortie-article-detail.html',
                    controller: 'LigneSortieArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneSortieArticle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LigneSortieArticle', function($stateParams, LigneSortieArticle) {
                    return LigneSortieArticle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ligne-sortie-article',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ligne-sortie-article-detail.edit', {
            parent: 'ligne-sortie-article-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-sortie-article/ligne-sortie-article-dialog.html',
                    controller: 'LigneSortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneSortieArticle', function(LigneSortieArticle) {
                            return LigneSortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-sortie-article.new', {
            parent: 'ligne-sortie-article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-sortie-article/ligne-sortie-article-dialog.html',
                    controller: 'LigneSortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                designation: null,
                                quantite: null,
                                montantht: null,
                                montanttva: null,
                                montantttc: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ligne-sortie-article', null, { reload: 'ligne-sortie-article' });
                }, function() {
                    $state.go('ligne-sortie-article');
                });
            }]
        })
        .state('ligne-sortie-article.edit', {
            parent: 'ligne-sortie-article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-sortie-article/ligne-sortie-article-dialog.html',
                    controller: 'LigneSortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneSortieArticle', function(LigneSortieArticle) {
                            return LigneSortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-sortie-article', null, { reload: 'ligne-sortie-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-sortie-article.delete', {
            parent: 'ligne-sortie-article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-sortie-article/ligne-sortie-article-delete-dialog.html',
                    controller: 'LigneSortieArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LigneSortieArticle', function(LigneSortieArticle) {
                            return LigneSortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-sortie-article', null, { reload: 'ligne-sortie-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
