(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('DecompositionDialogController', DecompositionDialogController);

    DecompositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Decomposition', 'Article'];

    function DecompositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Decomposition, Article) {
        var vm = this;

        vm.decomposition = entity;
        vm.clear = clear;
        vm.save = save;
        vm.articles = Article.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.decomposition.id !== null) {
                Decomposition.update(vm.decomposition, onSaveSuccess, onSaveError);
            } else {
                Decomposition.save(vm.decomposition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:decompositionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
