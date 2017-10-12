(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('FamilleArticleDeleteController',FamilleArticleDeleteController);

    FamilleArticleDeleteController.$inject = ['$uibModalInstance', 'entity', 'FamilleArticle'];

    function FamilleArticleDeleteController($uibModalInstance, entity, FamilleArticle) {
        var vm = this;

        vm.familleArticle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FamilleArticle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
