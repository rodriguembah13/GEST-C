'use strict';

describe('Controller Tests', function() {

    describe('SortieArticle Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSortieArticle, MockUser, MockMagasin, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSortieArticle = jasmine.createSpy('MockSortieArticle');
            MockUser = jasmine.createSpy('MockUser');
            MockMagasin = jasmine.createSpy('MockMagasin');
            MockClient = jasmine.createSpy('MockClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SortieArticle': MockSortieArticle,
                'User': MockUser,
                'Magasin': MockMagasin,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("SortieArticleDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestCApp:sortieArticleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
