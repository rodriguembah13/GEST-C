(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('TypeSortieArticleDialogController', TypeSortieArticleDialogController);

    TypeSortieArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TypeSortieArticle'];

    function TypeSortieArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TypeSortieArticle) {
        var vm = this;

        vm.typeSortieArticle = entity;
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
            if (vm.typeSortieArticle.id !== null) {
                TypeSortieArticle.update(vm.typeSortieArticle, onSaveSuccess, onSaveError);
            } else {
                TypeSortieArticle.save(vm.typeSortieArticle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:typeSortieArticleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
