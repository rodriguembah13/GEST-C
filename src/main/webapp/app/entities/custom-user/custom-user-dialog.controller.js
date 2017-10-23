(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('CustomUserDialogController', CustomUserDialogController);

    CustomUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'CustomUser', 'User'];

    function CustomUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, CustomUser, User) {
        var vm = this;

        vm.customUser = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customUser.id !== null) {
                CustomUser.update(vm.customUser, onSaveSuccess, onSaveError);
            } else {
                CustomUser.save(vm.customUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gestCApp:customUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPhoto = function ($file, customUser) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        customUser.photo = base64Data;
                        customUser.photoContentType = $file.type;
                    });
                });
            }
        };

    }
})();
