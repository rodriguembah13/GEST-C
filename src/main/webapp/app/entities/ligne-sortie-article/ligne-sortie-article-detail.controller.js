(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneSortieArticleDetailController', LigneSortieArticleDetailController);

    LigneSortieArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LigneSortieArticle', 'SortieArticle', 'Article', 'Client', 'TypeSortieArticle'];

    function LigneSortieArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, LigneSortieArticle, SortieArticle, Article, Client, TypeSortieArticle) {
        var vm = this;

        vm.ligneSortieArticle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:ligneSortieArticleUpdate', function(event, result) {
            vm.ligneSortieArticle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
