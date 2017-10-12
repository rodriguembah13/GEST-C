(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Article', 'FamilleArticle', 'FormeArticle', 'Etiquette'];

    function ArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, Article, FamilleArticle, FormeArticle, Etiquette) {
        var vm = this;

        vm.article = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
