(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('FournisseurController', FournisseurController);

    FournisseurController.$inject = ['$scope','$http','$state', 'Fournisseur', 'FournisseurSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams'];

    function FournisseurController($scope,$http,$state, Fournisseur, FournisseurSearch, ParseLinks, AlertService, paginationConstants, pagingParams) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;

        loadAll();

        function loadAll () {
            if (pagingParams.search) {
                FournisseurSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Fournisseur.query({
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
                vm.fournisseurs = data;
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

        function clear() {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }
         $scope.printListeFounisseur=function(){
        
       $http.get("/api/printListefournisseursPdf/",{responseType:'arraybuffer'})
        .success(function(data){
          var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');

        })
        .error(function(err){
          AlertService.error(err.message);
        });            

   };
            $scope.printListeFounisseurXls=function(){
        
       $http.get("/api/printListefournisseursXls/",{responseType:'arraybuffer'})
        .success(function(data){
          var file=new Blob([data],{type:'application/vnd.ms-excel'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');

        })
        .error(function(err){
          AlertService.error(err.message);
        });            

   }
    }
})();
