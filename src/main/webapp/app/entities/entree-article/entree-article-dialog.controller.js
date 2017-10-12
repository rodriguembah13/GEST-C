(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('EntreeArticleDialogController', EntreeArticleDialogController);

    EntreeArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EntreeArticle', 'User'];

    function EntreeArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EntreeArticle, User) {
        var vm = this;

        vm.entreeArticle = entity;
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
            if (vm.entreeArticle.id !== null) {
                EntreeArticle.update(vm.entreeArticle, onSaveSuccess, onSaveError);
            } else {
                EntreeArticle.save(vm.entreeArticle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:entreeArticleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateentre = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
