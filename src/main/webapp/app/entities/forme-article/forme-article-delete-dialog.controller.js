(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('FormeArticleDeleteController',FormeArticleDeleteController);

    FormeArticleDeleteController.$inject = ['$uibModalInstance', 'entity', 'FormeArticle'];

    function FormeArticleDeleteController($uibModalInstance, entity, FormeArticle) {
        var vm = this;

        vm.formeArticle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FormeArticle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
