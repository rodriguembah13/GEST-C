(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneCommandeDetailController', LigneCommandeDetailController);

    LigneCommandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LigneCommande', 'User', 'Article', 'Commande', 'Fournisseur', 'Magasin'];

    function LigneCommandeDetailController($scope, $rootScope, $stateParams, previousState, entity, LigneCommande, User, Article, Commande, Fournisseur, Magasin) {
        var vm = this;

        vm.ligneCommande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:ligneCommandeUpdate', function(event, result) {
            vm.ligneCommande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
