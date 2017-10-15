(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Facture', Facture);

    Facture.$inject = ['$resource', 'DateUtils'];

    function Facture ($resource, DateUtils) {
        var resourceUrl =  'api/factures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateedition = DateUtils.convertLocalDateFromServer(data.dateedition);
                        data.datefacturation = DateUtils.convertLocalDateFromServer(data.datefacturation);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateedition = DateUtils.convertLocalDateToServer(copy.dateedition);
                    copy.datefacturation = DateUtils.convertLocalDateToServer(copy.datefacturation);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateedition = DateUtils.convertLocalDateToServer(copy.dateedition);
                    copy.datefacturation = DateUtils.convertLocalDateToServer(copy.datefacturation);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('FactureU', FactureU);

    FactureU.$inject = ['$resource', 'DateUtils'];

    function FactureU ($resource, DateUtils) {
        var resourceUrl =  'api/factures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
