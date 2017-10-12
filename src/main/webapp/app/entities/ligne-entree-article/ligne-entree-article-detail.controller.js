(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('LigneEntreeArticleDetailController', LigneEntreeArticleDetailController);

    LigneEntreeArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LigneEntreeArticle', 'User', 'Article', 'EntreeArticle'];

    function LigneEntreeArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, LigneEntreeArticle, User, Article, EntreeArticle) {
        var vm = this;

        vm.ligneEntreeArticle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:ligneEntreeArticleUpdate', function(event, result) {
            vm.ligneEntreeArticle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
