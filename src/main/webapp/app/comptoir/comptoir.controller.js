(function() {
  'use strict';

  angular
  .module('gestCApp')
  .controller('ComptoirController', ComptoirController);

  ComptoirController.$inject = ['$scope', 'Principal', 'LoginService','AlertService', '$state','Article','$http','$q',
  'Caisse','CaisseA','Client','SortieArticle','SortieArticlePrint','CaisseActived','CaisseDesactived','StockActifByNum','StockGActifByNum','StockActifByCode'];

  function ComptoirController ($scope, Principal, LoginService,AlertService, $state,Article,$http,$q,Caisse,
    CaisseA,Client,SortieArticle,SortieArticlePrint,CaisseActived,CaisseDesactived,StockActifByNum,StockGActifByNum,StockActifByCode) {
    var vm = this;
    $scope.lines=[];$scope.linesCode=[];
    vm.$state = $state;
    vm.active=active;
    vm.validLine=validLine;
    vm.desactive=desactive;
    vm.caisseatif=CaisseA.query();
    vm.clients=Client.query();
    vm.validLineGros=validLineGros;vm.validLineDetail=validLineDetail;
    loadAllArticle ();
    vm.client = {}; vm.article = {};
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
             function validLineDetail (qte) {
                  var num=vm.article.numArticle;
              StockActifByNum.query({num_article: num,quantite:qte}
              , onSuccess, onError);
              function onSuccess(data) {
                 $scope.lines.push({
              article: data.article,
              quantite: qte,
              prix:data.article.prixCourant,
              tva: data.article.taxeTva,
              client: vm.client,
            })
              }function onError(error) {
                  AlertService.error("Stock insuffisant ,veuillez ravitailler le stock");
              }
            }
                function validLineGros (qte) {
                  var num=vm.article.numArticle;
              StockGActifByNum.query({num_article: num,quantite:qte}
              , onSuccess, onError);
              function onSuccess(data) {
                 $scope.linesCode.push({
              article: data.article,
              quantite: qte,
              prix:data.article.prixCourant,
              tva: data.article.taxeTva,
              client: vm.client,
            })
              }function onError(error) {
                  //AlertService.error(error.data.message);
              }
            }
       function validLine (num,qte) {
              StockActifByCode.query({num_article: num,quantite:qte}
              , onSuccess, onError);
              function onSuccess(data) {
                 $scope.linesCode.push({
              article: data.article,
              quantite: qte,
              prix:data.article.prixCourant,
              tva: data.article.taxeTva,
              client: vm.client,
            });$scope.code=null;$scope.qte=null;
              }function onError(error) {
                  AlertService.error(error.data.message);
              }
            }
            $scope.show = true;
            $scope.paie = false;
            $scope.closeAlert = function() {
             $scope.show = false;
           };

           $scope.alert = {type: 'success', msg: 'Something gone wrong'};

           function active(){
            CaisseActived.save({},onSuccess,onError);
            function onSuccess(data) {
            vm.caisse=data;
             $state.reload();
          }function onError(error) {
               console.log(err);
                }
           }
           function desactive(){
              CaisseDesactived.save({},onSuccess,onError)
              function onSuccess(data) {
           vm.caisse2=data;
             $state.reload();
          }function onError(error) {
               console.log(err);
                }
           }
           $scope.cancel = function() {
            for (var i = $scope.lines.length; i--;) {
              $scope.lines.splice(i, 1);

            };};
             $scope.cancelCode = function() {
            for (var i = $scope.linesCode.length; i--;) {
              $scope.linesCode.splice(i, 1);

            };
          };
  $scope.removec = function(index) {
            $scope.linesCode.splice(index, 1);
          };

           $scope.removeUser = function(index) {
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
  $scope.saveTable = function() {
            SortieArticle.save($scope.lines, onSaveSuccess,onSaveError);

    };
    function onSaveError (error) {
          AlertService.error(error.data.message,"vérifier le client");
                    }
     $scope.saveTableCode = function() {
            SortieArticle.save($scope.linesCode, onSaveSuccess);

    };
function onSaveSuccess (result) {
           $scope.venteE=result;
           $scope.paie = true;
                    }

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
       function PFSuccess (data) {
           var file=new Blob([data],{type:'application/pdf'});
          var fileUrl=URL.createObjectURL(file);
          var des = window.open(fileUrl,'_blank','');
          $scope.paie = false;
                    }
/*    var PrintF = $resource('/api/PrintFacture/':id,
             {
            'query': { method: 'GET', isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.arraybuffer(data);
                    }
                    return data;
                }}
        }
        );*/
    $scope.PrintFacture=function(etat){
      $scope.clas=$scope.venteE.id;

      if (etat=="fact1") {
        // SortieArticlePrint.query({id: $scope.clas},PFSuccess)
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
