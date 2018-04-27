(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('TransfertMagasinDetailController', TransfertMagasinDetailController);

    TransfertMagasinDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TransfertMagasin', 'Magasin', 'User'];

    function TransfertMagasinDetailController($scope, $rootScope, $stateParams, previousState, entity, TransfertMagasin, Magasin, User) {
        var vm = this;

        vm.transfertMagasin = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:transfertMagasinUpdate', function(event, result) {
            vm.transfertMagasin = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
