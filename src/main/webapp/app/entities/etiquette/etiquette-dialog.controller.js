(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('EtiquetteDialogController', EtiquetteDialogController);

    EtiquetteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Etiquette', 'Article'];

    function EtiquetteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Etiquette, Article) {
        var vm = this;

        vm.etiquette = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.articles = Article.query({filter: 'etiquette-is-null'});
        $q.all([vm.etiquette.$promise, vm.articles.$promise]).then(function() {
            if (!vm.etiquette.article || !vm.etiquette.article.id) {
                return $q.reject();
            }
            return Article.get({id : vm.etiquette.article.id}).$promise;
        }).then(function(article) {
            vm.articles.push(article);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.etiquette.id !== null) {
                Etiquette.update(vm.etiquette, onSaveSuccess, onSaveError);
            } else {
                Etiquette.save(vm.etiquette, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:etiquetteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateCreation = false;
        vm.datePickerOpenStatus.dateCrea = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
