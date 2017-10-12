(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('EntreeArticleDetailController', EntreeArticleDetailController);

    EntreeArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EntreeArticle', 'User'];

    function EntreeArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, EntreeArticle, User) {
        var vm = this;

        vm.entreeArticle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:entreeArticleUpdate', function(event, result) {
            vm.entreeArticle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
