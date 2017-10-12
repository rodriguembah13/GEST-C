(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('EntreeArticleDeleteController',EntreeArticleDeleteController);

    EntreeArticleDeleteController.$inject = ['$uibModalInstance', 'entity', 'EntreeArticle'];

    function EntreeArticleDeleteController($uibModalInstance, entity, EntreeArticle) {
        var vm = this;

        vm.entreeArticle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EntreeArticle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
