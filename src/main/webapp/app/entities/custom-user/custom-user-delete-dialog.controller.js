(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('CustomUserDeleteController',CustomUserDeleteController);

    CustomUserDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomUser'];

    function CustomUserDeleteController($uibModalInstance, entity, CustomUser) {
        var vm = this;

        vm.customUser = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomUser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
