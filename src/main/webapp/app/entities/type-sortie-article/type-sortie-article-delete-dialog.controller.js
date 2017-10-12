(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('TypeSortieArticleDeleteController',TypeSortieArticleDeleteController);

    TypeSortieArticleDeleteController.$inject = ['$uibModalInstance', 'entity', 'TypeSortieArticle'];

    function TypeSortieArticleDeleteController($uibModalInstance, entity, TypeSortieArticle) {
        var vm = this;

        vm.typeSortieArticle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TypeSortieArticle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
