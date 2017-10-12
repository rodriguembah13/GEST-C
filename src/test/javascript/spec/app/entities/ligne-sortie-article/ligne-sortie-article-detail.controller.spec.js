'use strict';

describe('Controller Tests', function() {

    describe('LigneSortieArticle Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLigneSortieArticle, MockSortieArticle, MockArticle, MockClient, MockTypeSortieArticle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLigneSortieArticle = jasmine.createSpy('MockLigneSortieArticle');
            MockSortieArticle = jasmine.createSpy('MockSortieArticle');
            MockArticle = jasmine.createSpy('MockArticle');
            MockClient = jasmine.createSpy('MockClient');
            MockTypeSortieArticle = jasmine.createSpy('MockTypeSortieArticle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LigneSortieArticle': MockLigneSortieArticle,
                'SortieArticle': MockSortieArticle,
                'Article': MockArticle,
                'Client': MockClient,
                'TypeSortieArticle': MockTypeSortieArticle
            };
            createController = function() {
                $injector.get('$controller')("LigneSortieArticleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestCApp:ligneSortieArticleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
