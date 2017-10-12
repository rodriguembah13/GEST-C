(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('SortieArticleDetailController', SortieArticleDetailController);

    SortieArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SortieArticle', 'User', 'Magasin', 'Client'];

    function SortieArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, SortieArticle, User, Magasin, Client) {
        var vm = this;

        vm.sortieArticle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:sortieArticleUpdate', function(event, result) {
            vm.sortieArticle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
