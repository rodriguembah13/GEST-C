(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('FormeArticleDialogController', FormeArticleDialogController);

    FormeArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FormeArticle'];

    function FormeArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FormeArticle) {
        var vm = this;

        vm.formeArticle = entity;
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
            if (vm.formeArticle.id !== null) {
                FormeArticle.update(vm.formeArticle, onSaveSuccess, onSaveError);
            } else {
                FormeArticle.save(vm.formeArticle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:formeArticleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
