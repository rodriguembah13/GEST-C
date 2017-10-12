(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('EtiquetteDetailController', EtiquetteDetailController);

    EtiquetteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Etiquette', 'Article'];

    function EtiquetteDetailController($scope, $rootScope, $stateParams, previousState, entity, Etiquette, Article) {
        var vm = this;

        vm.etiquette = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:etiquetteUpdate', function(event, result) {
            vm.etiquette = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
