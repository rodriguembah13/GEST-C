(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('TransfertMagasinDialogController', TransfertMagasinDialogController);

    TransfertMagasinDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TransfertMagasin', 'Magasin', 'User'];

    function TransfertMagasinDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TransfertMagasin, Magasin, User) {
        var vm = this;

        vm.transfertMagasin = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.magasins = Magasin.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transfertMagasin.id !== null) {
                TransfertMagasin.update(vm.transfertMagasin, onSaveSuccess, onSaveError);
            } else {
                TransfertMagasin.save(vm.transfertMagasin, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:transfertMagasinUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date_tranfert = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
