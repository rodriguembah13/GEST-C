(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneCommandeDialogController', LigneCommandeDialogController);

    LigneCommandeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LigneCommande', 'User', 'Article', 'Commande', 'Fournisseur'];

    function LigneCommandeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LigneCommande, User, Article, Commande, Fournisseur) {
        var vm = this;

        vm.ligneCommande = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.articles = Article.query();
        vm.commandes = Commande.query();
        vm.fournisseurs = Fournisseur.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ligneCommande.id !== null) {
                LigneCommande.update(vm.ligneCommande, onSaveSuccess, onSaveError);
            } else {
                LigneCommande.save(vm.ligneCommande, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:ligneCommandeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
