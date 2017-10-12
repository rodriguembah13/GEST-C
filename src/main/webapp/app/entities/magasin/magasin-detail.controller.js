(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('MagasinDetailController', MagasinDetailController);

    MagasinDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Magasin'];

    function MagasinDetailController($scope, $rootScope, $stateParams, previousState, entity, Magasin) {
        var vm = this;

        vm.magasin = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:magasinUpdate', function(event, result) {
            vm.magasin = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
