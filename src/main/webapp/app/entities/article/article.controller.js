(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('ArticleController', ArticleController);

    ArticleController.$inject = ['$scope','$http','$state', 'Article', 'ArticleSearch', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams','GenererCodeArticle'];

    function ArticleController($scope,$http,$state, Article, ArticleSearch, ParseLinks, AlertService, paginationConstants, pagingParams,GenererCodeArticle) {

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
        vm.genererCode=genererCode;

        loadAll();
        function genererCode (article) {
                GenererCodeArticle.update(article,onSuccess, onError);
                function onSuccess(data) {
                
                loadAll();
               
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        function loadAll () {
            if (pagingParams.search) {
                ArticleSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Article.query({
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
                vm.articles = data;
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
          $scope.PrintArticle=function(){
        
       $http.get("/api/printListeArticlePdf/",{responseType:'arraybuffer'})
        .success(function(data){
          var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');

        })
        .error(function(err){
          AlertService.error(err.message);
        });            

   }
    $scope.printListeArticleFamille=function(){
        
       $http.get("/api/printListeArticleFamillePdf/",{responseType:'arraybuffer'})
        .success(function(data){
          var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');

        })
        .error(function(err){
          AlertService.error(err.message);
        });            

   }
    }

})();
