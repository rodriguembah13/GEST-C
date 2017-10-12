(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('SortieArticleDialogController', SortieArticleDialogController);

    SortieArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SortieArticle', 'User', 'Magasin', 'Client'];

    function SortieArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SortieArticle, User, Magasin, Client) {
        var vm = this;

        vm.sortieArticle = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.magasins = Magasin.query();
        vm.clients = Client.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sortieArticle.id !== null) {
                SortieArticle.update(vm.sortieArticle, onSaveSuccess, onSaveError);
            } else {
                SortieArticle.save(vm.sortieArticle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:sortieArticleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datesortie = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
