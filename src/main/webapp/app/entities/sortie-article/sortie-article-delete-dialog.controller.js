(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('SortieArticleDeleteController',SortieArticleDeleteController);

    SortieArticleDeleteController.$inject = ['$uibModalInstance', 'entity', 'SortieArticle'];

    function SortieArticleDeleteController($uibModalInstance, entity, SortieArticle) {
        var vm = this;

        vm.sortieArticle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SortieArticle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
