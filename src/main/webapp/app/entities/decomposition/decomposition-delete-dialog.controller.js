(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('DecompositionDeleteController',DecompositionDeleteController);

    DecompositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Decomposition'];

    function DecompositionDeleteController($uibModalInstance, entity, Decomposition) {
        var vm = this;

        vm.decomposition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Decomposition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
