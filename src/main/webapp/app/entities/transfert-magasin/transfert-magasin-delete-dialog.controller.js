(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('TransfertMagasinDeleteController',TransfertMagasinDeleteController);

    TransfertMagasinDeleteController.$inject = ['$uibModalInstance', 'entity', 'TransfertMagasin'];

    function TransfertMagasinDeleteController($uibModalInstance, entity, TransfertMagasin) {
        var vm = this;

        vm.transfertMagasin = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TransfertMagasin.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
