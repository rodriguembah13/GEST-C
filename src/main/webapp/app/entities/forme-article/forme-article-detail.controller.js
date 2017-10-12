(function() {
    'use strict';

    angular
        .module('gestCApp')
        .controller('FormeArticleDetailController', FormeArticleDetailController);

    FormeArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FormeArticle'];

    function FormeArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, FormeArticle) {
        var vm = this;

        vm.formeArticle = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gestCApp:formeArticleUpdate', function(event, result) {
            vm.formeArticle = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
