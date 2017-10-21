(function() {
  'use strict';

  angular
  .module('gestCApp')
  .controller('ComptoirController', ComptoirController);

  ComptoirController.$inject = ['$scope', 'Principal', 'LoginService','AlertService', '$state','Article','$http','$q','Caisse','CaisseA','Client'];

  function ComptoirController ($scope, Principal, LoginService,AlertService, $state,Article,$http,$q,Caisse,CaisseA,Client) {
    var vm = this;
    $scope.lines=[];
    vm.$state = $state;
    vm.active=active;
    vm.desactive=desactive;
    vm.caisseatif=CaisseA.query();
    vm.clients=Client.query();
    loadAllArticle ();
    vm.client = {};
        //loadCaisse ();
        function loadAllArticle () {
          Article.query({
          }, onSuccess, onError);
          function onSuccess(data) {
            vm.articles = data;
          }function onError(error) {
                  //AlertService.error(error.data.message);
                }
              } 
/*         function loadCaisse () {
              CaisseA.query({
              }, onSuccess, onError);
              function onSuccess(data) {
                vm.caisseatif = data;
              }function onError(error) {
                  //AlertService.error(error.data.message);
              }
            } */
            $scope.show = true;
            $scope.paie = false;
            $scope.closeAlert = function() {
             $scope.show = false;
           };

           $scope.alert = {type: 'success', msg: 'Something gone wrong'};
           function active(){ $http.post('/api/caisses1')
           .success(function(data){
             vm.caisse=data;
             $state.reload();
           })
           .error(function(err){                       
            console.log(err);
          });}
           function desactive(){ $http.post('/api/caisses2')
           .success(function(data){
             vm.caisse2=data;
             $state.reload();
           })
           .error(function(err){
            console.log(err);                      
          });}
           $scope.cancel = function() {
            for (var i = $scope.lines.length; i--;) {
              $scope.lines.splice(i, 1);

            };
          };  $scope.removeUser = function(index) {
            $scope.lines.splice(index, 1);
          };
          $scope.addLine=function () {
            $scope.lines.push({
              article: null,
              quantite: null,
              prix:vm.listarticle,
              client: vm.client,
            })

          }
          function getStockActif(article){ $http.get('/api/stocksActif?article='+article.id)
          .success(function(data){
           vm.listarticle=data;
         })
          .error(function(err){
            console.log(err);
          });}
          $scope.saveUser = function(data, idL) {
      //getStockActif(idL);
      
    }
    $scope.venteE=null;
    $scope.venteR=null;
    // save edits
    $scope.saveTable = function() {
      var results = [];
      results.push($http.post('/api/sortie-articles',$scope.lines)
        .success(function(data){
          $scope.venteE=data;
                          $scope.paie = true;
                        })
        .error(function(err){
          console.log(err);
          AlertService.error(err.message);
        })
        );
      return $q.all(results);
    };   
    $scope.getTotal = function(){
      var total = 0;
      for(var i = 0; i < $scope.lines.length; i++){
          var user = $scope.lines[i];
          total += (user.article.prixCourant * user.quantite);
      }
      return total;
  }
      $scope.getTotalTTC = function(){
      var total = 0;
      for(var i = 0; i < $scope.lines.length; i++){
          var user = $scope.lines[i];
          var st=(user.article.prixCourant*user.article.taxeTva)*0.01
          total += (st+user.quantite*user.article.prixCourant);
      }
      return total;
  }
    $scope.mode = null;
    $scope.PrintFacture=function(etat){
      $scope.clas=$scope.venteE.id;
      if (etat=="fact1") {
         
        $http.get("/api/PrintFacture/"+$scope.clas,{responseType:'arraybuffer'})
        .success(function(data){
          var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');
          $scope.paie = false;
        })
        .error(function(err){
          AlertService.error(err.message);
        });
      }else{

       $http.get("/api/PrintTicket/"+$scope.clas,{responseType:'arraybuffer'})
       .success(function(data){
        var file=new Blob([data],{type:'application/pdf'});
        var fileUrl=URL.createObjectURL(file);
        var des = window.open(fileUrl,'_blank','');
             $scope.paie = false;                                          })
       .error(function(err){
        AlertService.error(err.message);
      });
     }             

   }
  
  }
})();
