(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('EtiquetteDeleteController',EtiquetteDeleteController);

    EtiquetteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Etiquette'];

    function EtiquetteDeleteController($uibModalInstance, entity, Etiquette) {
        var vm = this;

        vm.etiquette = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Etiquette.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
