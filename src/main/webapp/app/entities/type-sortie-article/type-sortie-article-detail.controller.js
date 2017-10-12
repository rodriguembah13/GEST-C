(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('TypeSortieArticleDetailController', TypeSortieArticleDetailController);

    TypeSortieArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TypeSortieArticle'];

    function TypeSortieArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, TypeSortieArticle) {
        var vm = this;

        vm.typeSortieArticle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:typeSortieArticleUpdate', function(event, result) {
            vm.typeSortieArticle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
