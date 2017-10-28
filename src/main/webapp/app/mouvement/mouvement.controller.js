(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('MouvementController',MouvementController);

    MouvementController.$inject = ['$scope', 'Principal', 'LoginService', '$state','Article','$http','$q','Fournisseur',
    'Magasin','EntreeArticleCom','EntreeArticle'];

    function MouvementController ($scope, Principal, LoginService, $state,Article,$http,$q,Fournisseur,Magasin,
      EntreeArticleCom,EntreeArticle) {
        var vm = this;
        $scope.lines=[];
        $scope.commandes=[];
       $scope.cmde = false;
       vm.fournisseur = {};
       vm.magasin = {};
       vm.fournisseurs= Fournisseur.query();
       vm.magasins=Magasin.query();
        loadAllArticle ();
      		function loadAllArticle () {
              Article.query({
              }, onSuccess, onError);
              function onSuccess(data) {
                vm.articles = data;
              }function onError(error) {
                  //AlertService.error(error.data.message);
              }
          }
              $scope.cancel = function() {
      for (var i = $scope.lines.length; i--;) {
            $scope.lines.splice(i, 1);
            
      };
    };  $scope.removeCommande = function(index) {
      $scope.commandes.splice(index, 1);
    };
     $scope.cancelC = function() {
      for (var i = $scope.commandes.length; i--;) {
            $scope.commandes.splice(i, 1);
            
      };
    };  $scope.removeUser = function(index) {
      $scope.lines.splice(index, 1);
    };
        $scope.addLine=function () {
              $scope.lines.push({
        		article: null,
        		quantite: null,
        		prix: null,
        		taxeTVA: null,
        		dateperemption:null,
        		prixAchat:null
              						})
              	$scope.open = function($event, elementOpened) {
		$event.preventDefault();
		$event.stopPropagation();

		$scope.opened[elementOpened] = !$scope.opened[elementOpened];
	};;
          }
            $scope.addLineCommande=function () {
              $scope.commandes.push({
            article: null,
            quantite: null,
            prix: null,
            taxeTva: null,
            fournisseur:vm.fournisseur,
            magasin:vm.magasin
                          })
          }
    $scope.getTotal = function(){
      var total = 0;
      for(var i = 0; i < $scope.lines.length; i++){
          var user = $scope.lines[i];
          total += (user.prix_unitaire * user.quantite);
      }
      return total;
  }
      $scope.getTotalTTC = function(){
      var total = 0;
      for(var i = 0; i < $scope.lines.length; i++){
          var user = $scope.lines[i];
          var st=(user.prix_unitaire*user.taxeTVA)*0.01
          total += (st+user.quantite*user.prix_unitaire);
      }
      return total;
  }
    $scope.venteE=null;
    $scope.venteR=null;
    // save edits
      $scope.saveTable = function() {
            EntreeArticle.save($scope.lines, onSaveSuccess);

    }; 
function onSaveSuccess (result) {
           $scope.venteE=result;
           $scope.PrintBordoreauR($scope.venteE);
                    }
/*    $scope.saveTable = function() {
      var results = [];
      results.push($http.post('/api/entree-articles',$scope.lines)
      	 .success(function(data){
                          $scope.venteE=data;
                          // $scope.PrintRecu($scope.venteE);
                          $scope.PrintBordoreauR($scope.venteE);
                        })
                        .error(function(err){
                        console.log(err);
                        AlertService.error(err.message);
                        })
                        );
      return $q.all(results);
    };
*/
        $scope.saveCommande = function() {
      var results = [];
      EntreeArticleCom.save($scope.commandes,onSuccess)
      function onSuccess (result) {
           $scope.venteR=result;
                          
                          $scope.PrintBordoreau($scope.venteR);
                    }
     /* results.push($http.post('/api/commandes',$scope.commandes)
         .success(function(data){
                          $scope.venteR=data;
                          // $scope.PrintRecu($scope.venteE);
                          $scope.PrintBordoreau($scope.venteR);
                        })
                        .error(function(err){
                        console.log(err);
                        //AlertService.error(err.message);
                        })
                        );
      return $q.all(results);*/
    };
/*popdate xedit*/
	$scope.getMinDate = function() {
		return new Date(1984, 4, 1);
	};

	$scope.getMaxDate = function($event, elementOpened) {
		return new Date(2016, 4, 1);
	};

	$scope.getInitDate = function($event, elementOpened) {
		return new Date();
	};

	$scope.altInputFormats = ['d/M/yy'];

	$scope.opened = {};


	
	$scope.changed = function(data) {
		$scope.user.changed = true;
		window.console.log("data = "+ data);
		window.console.log("value changed");
	};
      $scope.PrintBordoreau=function(cmd){
      //$scope.clas=$scope.venteE.id;
        $http.get("/api/printBdCmde/"+cmd.id,{responseType:'arraybuffer'})
        .success(function(data){
          var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');
         // $scope.paie = false;
        })
        .error(function(err){
          AlertService.error(err.message);
        });             

   };
         $scope.PrintBordoreauR=function(cmd){
      //$scope.clas=$scope.venteE.id;
        $http.get("/api/printBdReception/"+cmd.id,{responseType:'arraybuffer'})
        .success(function(data){
          var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');
         // $scope.paie = false;
        })
        .error(function(err){
          AlertService.error(err.message);
        });             

   }
    }
    // 

})();
