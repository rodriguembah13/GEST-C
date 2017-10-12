(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('DecompositionDetailController', DecompositionDetailController);

    DecompositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Decomposition', 'Article'];

    function DecompositionDetailController($scope, $rootScope, $stateParams, previousState, entity, Decomposition, Article) {
        var vm = this;

        vm.decomposition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:decompositionUpdate', function(event, result) {
            vm.decomposition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
