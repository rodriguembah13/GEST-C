(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('CaisseDetailController', CaisseDetailController);

    CaisseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Caisse', 'User'];

    function CaisseDetailController($scope, $rootScope, $stateParams, previousState, entity, Caisse, User) {
        var vm = this;

        vm.caisse = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:caisseUpdate', function(event, result) {
            vm.caisse = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
