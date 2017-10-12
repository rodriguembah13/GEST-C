(function() {
    'use strict';

    angular
        .module('gestCApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sortie-article', {
            parent: 'entity',
            url: '/sortie-article?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.sortieArticle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sortie-article/sortie-articles.html',
                    controller: 'SortieArticleController',
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
                    $translatePartialLoader.addPart('sortieArticle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sortie-article-detail', {
            parent: 'sortie-article',
            url: '/sortie-article/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gestCApp.sortieArticle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sortie-article/sortie-article-detail.html',
                    controller: 'SortieArticleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sortieArticle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SortieArticle', function($stateParams, SortieArticle) {
                    return SortieArticle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sortie-article',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sortie-article-detail.edit', {
            parent: 'sortie-article-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sortie-article/sortie-article-dialog.html',
                    controller: 'SortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SortieArticle', function(SortieArticle) {
                            return SortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sortie-article.new', {
            parent: 'sortie-article',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sortie-article/sortie-article-dialog.html',
                    controller: 'SortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numsortie: null,
                                libelle: null,
                                datesortie: null,
                                montanttotal: null,
                                montanttva: null,
                                montantttc: null,
                                destinataire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sortie-article', null, { reload: 'sortie-article' });
                }, function() {
                    $state.go('sortie-article');
                });
            }]
        })
        .state('sortie-article.edit', {
            parent: 'sortie-article',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sortie-article/sortie-article-dialog.html',
                    controller: 'SortieArticleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SortieArticle', function(SortieArticle) {
                            return SortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sortie-article', null, { reload: 'sortie-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sortie-article.delete', {
            parent: 'sortie-article',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sortie-article/sortie-article-delete-dialog.html',
                    controller: 'SortieArticleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SortieArticle', function(SortieArticle) {
                            return SortieArticle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sortie-article', null, { reload: 'sortie-article' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
