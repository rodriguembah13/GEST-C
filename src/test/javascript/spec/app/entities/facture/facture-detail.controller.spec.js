'use strict';

describe('Controller Tests', function() {

    describe('Facture Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFacture, MockClient, MockCaisse, MockUser, MockMagasin;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFacture = jasmine.createSpy('MockFacture');
            MockClient = jasmine.createSpy('MockClient');
            MockCaisse = jasmine.createSpy('MockCaisse');
            MockUser = jasmine.createSpy('MockUser');
            MockMagasin = jasmine.createSpy('MockMagasin');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Facture': MockFacture,
                'Client': MockClient,
                'Caisse': MockCaisse,
                'User': MockUser,
                'Magasin': MockMagasin
            };
            createController = function() {
                $injector.get('$controller')("FactureDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestCApp:factureUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
