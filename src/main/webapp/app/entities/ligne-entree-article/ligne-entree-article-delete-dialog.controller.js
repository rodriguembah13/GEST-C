(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneEntreeArticleDeleteController',LigneEntreeArticleDeleteController);

    LigneEntreeArticleDeleteController.$inject = ['$uibModalInstance', 'entity', 'LigneEntreeArticle'];

    function LigneEntreeArticleDeleteController($uibModalInstance, entity, LigneEntreeArticle) {
        var vm = this;

        vm.ligneEntreeArticle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LigneEntreeArticle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
