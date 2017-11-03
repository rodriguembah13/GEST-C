(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('SortieArticleDetailController', SortieArticleDetailController);

    SortieArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState',
    'entity', 'SortieArticle', 'User', 'Magasin', 'Client','LigneBySortieArticle','pagingParams',
    'paginationConstants','AlertService','ParseLinks','$state'];

    function SortieArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, 
        SortieArticle, User, Magasin, Client,LigneBySortieArticle,pagingParams,paginationConstants,
        AlertService,ParseLinks,$state) {
        var vm = this;

        vm.sortieArticle = entity;
        //vm.sortieArticles=sortieArticles;
        vm.previousState = previousState.name;
                vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.loadAll = loadAll;loadAll ();
        var unsubscribe = $rootScope.$on('gestCApp:sortieArticleUpdate', function(event, result) {
            vm.sortieArticle = result;
        });
                function loadAll () {
            
                LigneBySortieArticle.query({
                    //page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort(),
                    sortie:$stateParams.id
                }, onSuccess, onError);
            
         
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.sortieArticles = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }
        $scope.$on('$destroy', unsubscribe);
                function transition() {
        $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
