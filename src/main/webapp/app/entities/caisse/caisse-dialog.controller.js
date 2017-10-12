(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('CaisseDialogController', CaisseDialogController);

    CaisseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Caisse', 'User'];

    function CaisseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Caisse, User) {
        var vm = this;

        vm.caisse = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.caisse.id !== null) {
                Caisse.update(vm.caisse, onSaveSuccess, onSaveError);
            } else {
                Caisse.save(vm.caisse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:caisseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateOuverture = false;
        vm.datePickerOpenStatus.dateFermeture = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
