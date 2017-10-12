(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('FamilleArticleDetailController', FamilleArticleDetailController);

    FamilleArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FamilleArticle'];

    function FamilleArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, FamilleArticle) {
        var vm = this;

        vm.familleArticle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:familleArticleUpdate', function(event, result) {
            vm.familleArticle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
