(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Caisse', Caisse)
        .factory('CaisseA', CaisseA)
        .factory('CaisseActived', CaisseActived)
        .factory('CaisseDesactived', CaisseDesactived);

    Caisse.$inject = ['$resource', 'DateUtils'];
    CaisseA.$inject = ['$resource', 'DateUtils'];
    CaisseActived.$inject = ['$resource'];
    CaisseDesactived.$inject = ['$resource'];
    function Caisse ($resource, DateUtils) {
        var resourceUrl =  'api/caisses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOuverture = DateUtils.convertLocalDateFromServer(data.dateOuverture);
                        data.dateFermeture = DateUtils.convertLocalDateFromServer(data.dateFermeture);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateOuverture = DateUtils.convertLocalDateToServer(copy.dateOuverture);
                    copy.dateFermeture = DateUtils.convertLocalDateToServer(copy.dateFermeture);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateOuverture = DateUtils.convertLocalDateToServer(copy.dateOuverture);
                    copy.dateFermeture = DateUtils.convertLocalDateToServer(copy.dateFermeture);
                    return angular.toJson(copy);
                }
            }
        });
    }   
     function CaisseA ($resource, DateUtils) {
        var resourceUrl =  'api/caisse/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: false}
        });
    }
         function CaisseActived ($resource) {
        var resourceUrl =  'api/caisses1/:id';

        return $resource(resourceUrl, {}, {
            'save': { method: 'POST'}
        });
    }         function CaisseDesactived ($resource) {
        var resourceUrl =  'api/caisses2/:id';

        return $resource(resourceUrl, {}, {
            'save': { method: 'POST'}
        });
    }
})();
