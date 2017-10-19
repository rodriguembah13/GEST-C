'use strict';

describe('Controller Tests', function() {

    describe('LigneCommande Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockLigneCommande, MockUser, MockArticle, MockCommande, MockFournisseur, MockMagasin;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockLigneCommande = jasmine.createSpy('MockLigneCommande');
            MockUser = jasmine.createSpy('MockUser');
            MockArticle = jasmine.createSpy('MockArticle');
            MockCommande = jasmine.createSpy('MockCommande');
            MockFournisseur = jasmine.createSpy('MockFournisseur');
            MockMagasin = jasmine.createSpy('MockMagasin');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'LigneCommande': MockLigneCommande,
                'User': MockUser,
                'Article': MockArticle,
                'Commande': MockCommande,
                'Fournisseur': MockFournisseur,
                'Magasin': MockMagasin
            };
            createController = function() {
                $injector.get('$controller')("LigneCommandeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'gestCApp:ligneCommandeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
