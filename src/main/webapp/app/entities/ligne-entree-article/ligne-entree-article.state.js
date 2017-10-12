(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ligne-entree-article', {
            parent: 'entity',
            url: '/ligne-entree-article?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.ligneEntreeArticle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-entree-article/ligne-entree-articles.html',
                    controller: 'LigneEntreeArticleController',
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
                    $translatePartialLoader.addPart('ligneEntreeArticle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ligne-entree-article-detail', {
            parent: 'ligne-entree-article',
            url: '/ligne-entree-article/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.ligneEntreeArticle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ligne-entree-article/ligne-entree-article-detail.html',
                    controller: 'LigneEntreeArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ligneEntreeArticle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LigneEntreeArticle', function($stateParams, LigneEntreeArticle) {
                    return LigneEntreeArticle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ligne-entree-article',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ligne-entree-article-detail.edit', {
            parent: 'ligne-entree-article-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-entree-article/ligne-entree-article-dialog.html',
                    controller: 'LigneEntreeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneEntreeArticle', function(LigneEntreeArticle) {
                            return LigneEntreeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-entree-article.new', {
            parent: 'ligne-entree-article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-entree-article/ligne-entree-article-dialog.html',
                    controller: 'LigneEntreeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                designation: null,
                                quantite: null,
                                montanttotalht: null,
                                montanttotalttc: null,
                                prix_unitaire: null,
                                taxeTVA: null,
                                dateperemption: null,
                                prixAchat: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ligne-entree-article', null, { reload: 'ligne-entree-article' });
                }, function() {
                    $state.go('ligne-entree-article');
                });
            }]
        })
        .state('ligne-entree-article.edit', {
            parent: 'ligne-entree-article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-entree-article/ligne-entree-article-dialog.html',
                    controller: 'LigneEntreeArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LigneEntreeArticle', function(LigneEntreeArticle) {
                            return LigneEntreeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-entree-article', null, { reload: 'ligne-entree-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ligne-entree-article.delete', {
            parent: 'ligne-entree-article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ligne-entree-article/ligne-entree-article-delete-dialog.html',
                    controller: 'LigneEntreeArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LigneEntreeArticle', function(LigneEntreeArticle) {
                            return LigneEntreeArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ligne-entree-article', null, { reload: 'ligne-entree-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
