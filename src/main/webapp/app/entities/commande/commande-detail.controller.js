(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('CommandeDetailController', CommandeDetailController);

    CommandeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Commande', 'User', 'Fournisseur', 'Magasin'];

    function CommandeDetailController($scope, $rootScope, $stateParams, previousState, entity, Commande, User, Fournisseur, Magasin) {
        var vm = this;

        vm.commande = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:commandeUpdate', function(event, result) {
            vm.commande = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
