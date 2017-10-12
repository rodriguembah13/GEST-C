(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneSortieArticleDialogController', LigneSortieArticleDialogController);

    LigneSortieArticleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LigneSortieArticle', 'SortieArticle', 'Article', 'Client', 'TypeSortieArticle'];

    function LigneSortieArticleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LigneSortieArticle, SortieArticle, Article, Client, TypeSortieArticle) {
        var vm = this;

        vm.ligneSortieArticle = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sortiearticles = SortieArticle.query();
        vm.articles = Article.query();
        vm.clients = Client.query();
        vm.typesortiearticles = TypeSortieArticle.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ligneSortieArticle.id !== null) {
                LigneSortieArticle.update(vm.ligneSortieArticle, onSaveSuccess, onSaveError);
            } else {
                LigneSortieArticle.save(vm.ligneSortieArticle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:ligneSortieArticleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
