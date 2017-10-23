(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('CustomUserDetailController', CustomUserDetailController);

    CustomUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'CustomUser', 'User'];

    function CustomUserDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, CustomUser, User) {
        var vm = this;

        vm.customUser = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('gestCApp:customUserUpdate', function(event, result) {
            vm.customUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
