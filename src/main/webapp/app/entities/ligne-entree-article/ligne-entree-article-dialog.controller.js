(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneEntreeArticleDialogController', LigneEntreeArticleDialogController);

    LigneEntreeArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LigneEntreeArticle', 'User', 'Article', 'EntreeArticle'];

    function LigneEntreeArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LigneEntreeArticle, User, Article, EntreeArticle) {
        var vm = this;

        vm.ligneEntreeArticle = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.articles = Article.query();
        vm.entreearticles = EntreeArticle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ligneEntreeArticle.id !== null) {
                LigneEntreeArticle.update(vm.ligneEntreeArticle, onSaveSuccess, onSaveError);
            } else {
                LigneEntreeArticle.save(vm.ligneEntreeArticle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:ligneEntreeArticleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateperemption = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
