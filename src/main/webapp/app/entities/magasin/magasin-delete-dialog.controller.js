(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('MagasinDeleteController',MagasinDeleteController);

    MagasinDeleteController.$inject = ['$uibModalInstance', 'entity', 'Magasin'];

    function MagasinDeleteController($uibModalInstance, entity, Magasin) {
        var vm = this;

        vm.magasin = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Magasin.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
