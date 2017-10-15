(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('MouvementController',MouvementController);

    MouvementController.$inject = ['$scope', 'Principal', 'LoginService', '$state','Article','$http','$q'];

    function MouvementController ($scope, Principal, LoginService, $state,Article,$http,$q) {
        var vm = this;
        $scope.lines=[];
       
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
    };  $scope.removeUser = function(index) {
      $scope.lines.splice(index, 1);
    };
        $scope.addLine=function () {
              $scope.lines.push({
        		article: null,
        		quantite: null,
        		prix_unitaire: null,
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
      var results = [];
      results.push($http.post('/api/entree-articles',$scope.lines)
      	 .success(function(data){
                          $scope.venteE=data;
                          // $scope.PrintRecu($scope.venteE);
                        })
                        .error(function(err){
                        console.log(err);
                        AlertService.error(err.message);
                        })
                        );
      return $q.all(results);
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
    }
    // 

})();
