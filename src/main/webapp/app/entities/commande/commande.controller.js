(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('CommandeController', CommandeController);

    CommandeController.$inject = ['$scope','$state', 'Commande', 'CommandeSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','$http','$q','SortieArticlePrint'];

    function CommandeController($scope,$state, Commande, CommandeSearch, ParseLinks, AlertService, paginationConstants, pagingParams,$http,$q,SortieArticlePrint) {

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
                CommandeSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Commande.query({
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
                vm.commandes = data;
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
        }       function PFSuccess (result) {
           var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');
          $scope.paie = false;
                    } 
        $scope.PrintBordoreau=function(cmd){
      //$scope.clas=$scope.venteE.id;
        //$http.get("/api/printBdCmde/"+cmd.id,{responseType:'arraybuffer'})
        SortieArticlePrint.query({id:cmd.id,
            responseType:'arraybuffer'},PFSuccess)
       /*.success(function(data){
          var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');
         // $scope.paie = false;
        }) */
        .error(function(err){
          AlertService.error(err.message);
        });             

   }
    }
})();
