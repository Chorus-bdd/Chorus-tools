var featureUtility = angular.module('views.featureUtility', ['restangular', 'ngRoute', 'chorus.features']);

featureUtility.config(function($locationProvider, $routeProvider, RestangularProvider) {

        $locationProvider.html5Mode = true;

        //$routeProvider
        //    .when('/',    {templateUrl: '/app/views/featureUtility/form.html',
        //                   controller: chorus.view.FeaturesController, controllerAs: 'feature' })
        //    .when('/Feature/:featureId',
        //                  {templateUrl: '/app/views/featureUtility/form.html',
        //                   controller: chorus.view.FeaturesController, controllerAs: 'feature' })
        //    .otherwise(   {redirectTo: '/'});

    $routeProvider
        .when('/Package/:packageId',
                     {templateUrl: '/app/views/featureUtility/viewpackage.html',
                      controller: chorus.ViewPackageController, controllerAs: 'package', reloadOnSearch: false })
        .when('/Package/:packageId/create',
                     {templateUrl: '/app/views/featureUtility/createpackage.html',
                      controller: chorus.CreatePackageController, controllerAs: 'package', reloadOnSearch: false })
        .when('/Package/:packageId/move',
                     {templateUrl: '/app/views/featureUtility/movefeature.html',
                      controller: chorus.MovePackageController, controllerAs: 'feature', reloadOnSearch: false })
        .when('/Feature/:featureId',
                      {templateUrl: '/app/views/featureUtility/viewfeature.html',
                       controller: chorus.ViewFeatureController, controllerAs: 'feature', reloadOnSearch: false })
        .when('/Package/:packageId/createFeature',
                      {templateUrl: '/app/views/featureUtility/editfeature.html',
                       controller: chorus.CreateFeatureController, controllerAs: 'feature', reloadOnSearch: false })
        .when('/Feature/:featureId/edit',
                      {templateUrl: '/app/views/featureUtility/editfeature.html',
                       controller: chorus.EditFeatureController, controllerAs: 'feature', reloadOnSearch: false })
        .when('/Feature/:featureId/move',
                      {templateUrl: '/app/views/featureUtility/movefeature.html',
                       controller: chorus.MoveFeatureController, controllerAs: 'feature', reloadOnSearch: false })
        .otherwise(   {redirectTo: '/'});

    RestangularProvider.setBaseUrl('http://localhost:8085');
    RestangularProvider.setRequestInterceptor(function(elem, operation, what) {
        if (operation === 'put') {
            elem._id = undefined;
            return elem;
        }
        return elem;
    });
});


featureUtility.value('BASE_PATH', "/app/views/featureUtility");

featureUtility.service('FeatureRedirector', ['$location', FeatureRedirector]);