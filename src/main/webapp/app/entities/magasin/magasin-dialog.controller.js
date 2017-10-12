(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('MagasinDialogController', MagasinDialogController);

    MagasinDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Magasin'];

    function MagasinDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Magasin) {
        var vm = this;

        vm.magasin = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.magasin.id !== null) {
                Magasin.update(vm.magasin, onSaveSuccess, onSaveError);
            } else {
                Magasin.save(vm.magasin, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:magasinUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
