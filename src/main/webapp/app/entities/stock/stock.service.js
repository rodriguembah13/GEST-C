(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('Stock', Stock);

    Stock.$inject = ['$resource', 'DateUtils'];

    function Stock ($resource, DateUtils) {
        var resourceUrl =  'api/stocks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateperemption = DateUtils.convertLocalDateFromServer(data.dateperemption);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateperemption = DateUtils.convertLocalDateToServer(copy.dateperemption);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dateperemption = DateUtils.convertLocalDateToServer(copy.dateperemption);
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
        .factory('StockA', StockA)
        .factory('Stockclosed', Stockclosed);

    StockA.$inject = ['$resource', 'DateUtils'];
    Stockclosed.$inject = ['$resource', 'DateUtils'];
    function StockA ($resource, DateUtils) {
        var resourceUrl =  'api/stocksA/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    //copy.dateperemption = DateUtils.convertLocalDateToServer(copy.dateperemption);
                    return angular.toJson(copy);
                }
            }
        });
    }
function Stockclosed ($resource, DateUtils) {
        var resourceUrl =  'api/stocksC/:id';

        return $resource(resourceUrl, {}, {
             'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    //copy.dateperemption = DateUtils.convertLocalDateToServer(copy.dateperemption);
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
        .factory('StockP', StockP);

    StockP.$inject = ['$resource', 'DateUtils'];

    function StockP ($resource, DateUtils) {
        var resourceUrl =  'api/stocksP/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
(function() {
    'use strict';
    angular
        .module('gestCApp')
        .factory('StockP2', StockP2);

    StockP2.$inject = ['$resource', 'DateUtils'];

    function StockP2 ($resource, DateUtils) {
        var resourceUrl =  'api/stocksP2/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();