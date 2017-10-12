(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('FactureDetailController', FactureDetailController);

    FactureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Facture', 'Client', 'Caisse', 'User', 'Magasin'];

    function FactureDetailController($scope, $rootScope, $stateParams, previousState, entity, Facture, Client, Caisse, User, Magasin) {
        var vm = this;

        vm.facture = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:factureUpdate', function(event, result) {
            vm.facture = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
