(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('SortieArticleController', SortieArticleController);

    SortieArticleController.$inject = ['$filter','$state','$scope','$http','SortieArticle', 'SortieArticleSearch', 'ParseLinks', 'AlertService', 
    'paginationConstants', 'pagingParams','SortieArticleU','SortieArticlePrint','SortieArticleByDate','SortieArticleByDateUser'];

    function SortieArticleController($filter,$state,$scope,$http, SortieArticle, SortieArticleSearch, ParseLinks, AlertService,
     paginationConstants, pagingParams,SortieArticleU,SortieArticlePrint,SortieArticleByDate,SortieArticleByDateUser) {

        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;vm.transition1 = transition1;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.clear = clear;
        vm.search = search;
        vm.loadAll = loadAll;
        vm.loadAllByDate = loadAllByDate;
        vm.loadAllByDateUser= loadAllByDateUser;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
                vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        loadAll();
        vm.sorties=SortieArticleU.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage
                });
                        function loadAllByDateUser (dateDeb,dateF) {
             var dateFormat = 'yyyy-MM-dd';
            var fromDate = $filter('date')(dateDeb, dateFormat);
            var toDate = $filter('date')(dateF, dateFormat);
                SortieArticleByDateUser.query({
                    dateDebut:fromDate,
                    dateFin:toDate,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
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
                vm.sorties = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
                function loadAllByDate (dateDeb,dateF) {
             var dateFormat = 'yyyy-MM-dd';
            var fromDate = $filter('date')(dateDeb, dateFormat);
            var toDate = $filter('date')(dateF, dateFormat);
                SortieArticleByDate.query({
                    dateDebut:fromDate,
                    dateFin:toDate,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
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
                vm.page1 = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        function loadAll () {
            if (pagingParams.search) {
                SortieArticleSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                SortieArticle.query({
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
         
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'desc' : 'asc')];
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
        }

        function loadPage(page) {
            vm.page = page;vm.page1 = page;
            vm.transition();vm.transition1();
        }
        vm.datePickerOpenStatus.fromDate = false;
        vm.datePickerOpenStatus.toDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
  function transition1() {
            $state.transitionTo($state.$current, {
                page: vm.page1,
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
          $scope.PrintFacture=function(vente){
      $scope.clas=vente;  
       $http.get("/api/PrintFacture/"+$scope.clas,{responseType:'arraybuffer'})
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
