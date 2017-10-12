'use strict';

describe('Controller Tests', function() {

    describe('LigneEntreeArticle Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLigneEntreeArticle, MockUser, MockArticle, MockEntreeArticle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLigneEntreeArticle = jasmine.createSpy('MockLigneEntreeArticle');
            MockUser = jasmine.createSpy('MockUser');
            MockArticle = jasmine.createSpy('MockArticle');
            MockEntreeArticle = jasmine.createSpy('MockEntreeArticle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LigneEntreeArticle': MockLigneEntreeArticle,
                'User': MockUser,
                'Article': MockArticle,
                'EntreeArticle': MockEntreeArticle
            };
            createController = function() {
                $injector.get('$controller')("LigneEntreeArticleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestCApp:ligneEntreeArticleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
