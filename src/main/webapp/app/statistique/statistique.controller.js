(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('StatistiqueController', StatistiqueController);

    StatistiqueController.$inject = ['$filter','$scope','$state','$http','$q','LigneSortieStat','LigneSortieStats','LigneSortieStatAchatClient',
    'LigneSortieStatyearParam','LigneSortieStatmonthParam','LigneSortieStatmonth'];

    function StatistiqueController($filter,$scope,$state,$http,$q,LigneSortieStat,LigneSortieStats,LigneSortieStatAchatClient,
      LigneSortieStatyearParam,LigneSortieStatmonthParam,LigneSortieStatmonth) {
        var vm = this;
        //vm.fromDate = null;
        vm.loadAll = loadAll;
        vm.loadSatCom=loadSatCom;
        vm.loadSatStock=loadSatStock;
        vm.loadStatClient=loadStatClient;
        vm.loadStatVenteEnMomtantyear=loadStatVenteEnMomtantyear;
        vm.loadStatVenteYear=loadStatVenteYear;
        vm.loadStatVenteYearParam=loadStatVenteYearParam;
        vm.loadStatVenteEnMomtantyearParam=loadStatVenteEnMomtantyearParam;
        vm.loadStatVenteMonthParam=loadStatVenteMonthParam;
        vm.loadStatVenteMonth=loadStatVenteMonth;
        $scope.listyear=[{value: 1, text: '2016'},
    {value: 2, text: '2017'},
    {value: 3, text: '2018'},
    {value: 4, text: '2019'}]
        $scope.listmois=[{value: 1, text: 'janvier'},
    {value: 2, text: 'fevrier'},
    {value: 3, text: 'mars'},
    {value: 4, text: 'Avril'},{value: 5, text: 'mai'},{value: 6, text: 'juin'},{value: 7, text: 'juillet'},
    {value: 8, text: 'Aout'},{value: 9, text: 'Septembre'},{value: 10, text: 'Octobre'},{value: 11, text: 'novembre'},{value: 12, text: 'Decembre'}]
        loadAll ();
        loadSatCom();
        loadSatStock();
        loadStatClient();
        loadStatVenteEnMomtantyear();
        loadStatVenteYear();loadStatVenteMonth();
                            function loadStatVenteMonthParam(fromYear,fromMonth){
                 LigneSortieStatmonthParam.query({year: fromYear,monthValue:fromMonth}, function(res){
                                  var selected = [];
                                 var selectedL = [];var total=0.0;
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);total+=value;
                                     });  
                                   $scope.dataVenteMois = selected;
                                   $scope.labelsVenteMois =selectedL;
                                   $scope.ttc=total;
                                            })
                                  ;}
         function loadStatVenteMonth(){
                 LigneSortieStatmonth.query(function(res){
                                  var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.dataVenteMois = selected;
                                   $scope.labelsVenteMois =selectedL;
                                            })
                                  ;}
                    function loadStatVenteYearParam(fromDate){
                 LigneSortieStat.query({year: fromDate}, function(res){
                                  var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.dataVyear = selected;
                                   $scope.labelsVyear =selectedL;
                                            })
                                  ;}
      
                                     function loadStatVenteYear () {
                                     LigneSortieStats.query(function(res){
                                  var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.dataVyear = selected;
                                   $scope.labelsVyear =selectedL;});
                               };
                             function loadStatClient () {
                            LigneSortieStatAchatClient.query(function(res){
                                  var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.dataclient = selected;
                                   $scope.labelsclient =selectedL;});
                                  };
                               function loadStatVenteEnMomtantyear () {
                               //$scope.dte=data;
                              // angular.extend(data, {id: id});
                                 $http.get("api/stat-vente-montant-year")
                                 .success(function(res) {
                                 
                                 //$scope.data =[res];
                                 var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.dataclientM = selected;
                                   $scope.labelsclientM =selectedL;
                                 }).error(function(e){
                                   /*d.reject('Server error!');*/
                                   console.log(e);
                                 });

                               };
                                function loadStatVenteEnMomtantyearParam (fromDate) {
                               LigneSortieStatyearParam.query({year: fromDate},function(res){
                                  var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.dataclientM = selected;
                                   $scope.labelsclientM =selectedL;});
                                  };
                     function loadAll () {
                               //$scope.dte=data;
                              // angular.extend(data, {id: id});
                                 $http.get("api/stat-produit-vente")
                                 .success(function(res) {
                                 
                                 //$scope.data =[res];
                                 var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.data = selected;
                                   $scope.labels =selectedL;
                                 }).error(function(e){
                                   /*d.reject('Server error!');*/
                                   console.log(e);
                                 });

                               };
                function loadSatCom () {
                               //$scope.dte=data;
                              // angular.extend(data, {id: id});
                                 $http.get("api/stat-commande")
                                 .success(function(res) {
                                 
                                 //$scope.data =[res];
                                 var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.data1 = selected;
                                   $scope.labels1 =selectedL;
                                 }).error(function(e){
                                   /*d.reject('Server error!');*/
                                   console.log(e);
                                 });

                               };
                function loadSatStock () {
                               //$scope.dte=data;
                              // angular.extend(data, {id: id});
                                 $http.get("api/stat-stock")
                                 .success(function(res) {
                                 
                                 //$scope.data =[res];
                                 var selected = [];
                                 var selectedL = [];
                               angular.forEach(res, function(value, key) {
                                     selected.push(value);
                                     selectedL.push(key);
                                     });  
                                   $scope.data2 = selected;
                                   $scope.labels2 =selectedL;
                                 }).error(function(e){
                                   /*d.reject('Server error!');*/
                                   console.log(e);
                                 });

                               };
        function onChangeDate () {
            var dateFormat = 'yyyy-MM-dd';
            var fromDate = $filter('date')(vm.fromDate, dateFormat);
            var toDate = $filter('date')(vm.toDate, dateFormat);
                
        }$scope.colors = ['#45b7cd', '#ff6384', '#ff8e72'];     
    }
})();
