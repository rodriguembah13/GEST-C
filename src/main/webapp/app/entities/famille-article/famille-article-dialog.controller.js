(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('FamilleArticleDialogController', FamilleArticleDialogController);

    FamilleArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FamilleArticle'];

    function FamilleArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FamilleArticle) {
        var vm = this;

        vm.familleArticle = entity;
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
            if (vm.familleArticle.id !== null) {
                FamilleArticle.update(vm.familleArticle, onSaveSuccess, onSaveError);
            } else {
                FamilleArticle.save(vm.familleArticle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:familleArticleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
