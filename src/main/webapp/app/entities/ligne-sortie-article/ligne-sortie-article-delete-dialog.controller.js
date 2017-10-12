(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneSortieArticleDeleteController',LigneSortieArticleDeleteController);

    LigneSortieArticleDeleteController.$inject = ['$uibModalInstance', 'entity', 'LigneSortieArticle'];

    function LigneSortieArticleDeleteController($uibModalInstance, entity, LigneSortieArticle) {
        var vm = this;

        vm.ligneSortieArticle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LigneSortieArticle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
