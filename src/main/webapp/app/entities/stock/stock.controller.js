(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('StockController', StockController);

    StockController.$inject = ['$state', 'Stock', 'StockSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$http','$scope','Decomposition'];

    function StockController($state, Stock, StockSearch, ParseLinks, AlertService, paginationConstants, pagingParams,$http,$scope,Decomposition) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.active = active;
        vm.save = save;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;

        loadAll();
                function save1 (article) {

                Decomposition.save(article);
    
        }
        function save (article) {$http.post('/api/decompositions',article)
                      .success(function(data){
                         vm.stockDecomp=data;
                          loadAll();
                      })
                      .error(function(err){
                      console.log(err);
                      });}
        function active(st){ $http.put('/api/stocksA?stock='+st)
                      .success(function(data){
                         vm.stockActif=data;
                          vm.loadAll();
                      })
                      .error(function(err){
                      console.log(err);
                      });}
        function loadAll () {
            if (pagingParams.search) {
                StockSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Stock.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
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
                vm.stocks = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search(searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }
             $scope.getClass = function (strValue,alerteValue) {
                          if (strValue <= alerteValue)
                            return "Red";
                            else
                               return "";
                         }
        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
                  $scope.getClass = function (strValue) {
                          if (strValue)
                            return "Green";
                            else
                               return "";
                         };
    }
})();