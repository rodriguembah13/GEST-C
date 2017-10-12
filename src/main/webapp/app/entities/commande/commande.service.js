(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Commande', Commande);

    Commande.$inject = ['$resource', 'DateUtils'];

    function Commande ($resource, DateUtils) {
        var resourceUrl =  'api/commandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.datelimitlivraison = DateUtils.convertLocalDateFromServer(data.datelimitlivraison);
                        data.datecommande = DateUtils.convertLocalDateFromServer(data.datecommande);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.datelimitlivraison = DateUtils.convertLocalDateToServer(copy.datelimitlivraison);
                    copy.datecommande = DateUtils.convertLocalDateToServer(copy.datecommande);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.datelimitlivraison = DateUtils.convertLocalDateToServer(copy.datelimitlivraison);
                    copy.datecommande = DateUtils.convertLocalDateToServer(copy.datecommande);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
