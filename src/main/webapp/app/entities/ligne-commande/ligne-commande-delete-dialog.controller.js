(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneCommandeDeleteController',LigneCommandeDeleteController);

    LigneCommandeDeleteController.$inject = ['$uibModalInstance', 'entity', 'LigneCommande'];

    function LigneCommandeDeleteController($uibModalInstance, entity, LigneCommande) {
        var vm = this;

        vm.ligneCommande = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LigneCommande.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
