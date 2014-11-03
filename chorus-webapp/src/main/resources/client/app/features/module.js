var module = angular.module('chorus.features', ['restangular']);

module.config(function(RestangularProvider) {
        RestangularProvider.setBaseUrl('http://localhost:8085');
        RestangularProvider.setRequestInterceptor(function(elem, operation, what) {
            if (operation === 'put') {
                elem._id = undefined;
                return elem;
            }
            return elem;
        });
    });

module.service('FeatureService', ['Restangular', chorus.FeatureService]);
module.service('PackageService', ['Restangular', chorus.PackageService]);

//module.factory('FeatureService', ['Restangular', function(Restangular) {
//    return new chorus.FeatureService(Restangular);
//}]);
