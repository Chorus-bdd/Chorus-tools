namespace('chorus');

chorus.ViewPackageController = function($routeParams, PackageService, FeatureRedirector, $scope) {
    Preconditions.checkNotNull($routeParams, "$routeParams must not be null");
    Preconditions.checkNotNull(PackageService, "PackageService must not be null");
    Preconditions.checkNotNull(FeatureRedirector, "FeatureRedirector must not be null");
    this.FeatureRedirector = FeatureRedirector;
    this.PackageService = PackageService;
    this.$routeParams = $routeParams;

    this.packageId = this._getPackageRouteParam();
    var me = this;
    this.PackageService.findById(this.packageId, function(pakage) {
        //$scope.$apply(function() {
            me.package = pakage;
            me.name = me.package.name;
            me.parentPackages = me.PackageService.parents(me.package);
            //me.features = me.package.features;
            //me.subpackages = me.package.subpackages;
        //});
    });
};


chorus.ViewPackageController.prototype.viewPackage = function(packageId) {
    return this.FeatureRedirector.viewPackage(packageId);
};

chorus.ViewPackageController.prototype.viewFeature = function(featureId) {
    return this.FeatureRedirector.viewFeature(featureId);
};

chorus.ViewPackageController.prototype.newPackage = function() {
    this.FeatureRedirector.newPackage(this.packageId);
};

chorus.ViewPackageController.prototype.newFeature = function() {
    this.FeatureRedirector.newFeature(this.packageId);
};

chorus.ViewPackageController.prototype.move = function() {
    this.FeatureRedirector.movePackage(this.packageId);
};

chorus.ViewPackageController.prototype.delete = function() {
    var parent = this.package.parent;
    if (parent == null) {
        throw { name: "Package Delete Error", message: "Unable to delete top level package" };
    }
    this.PackageService.delete(this.packageId);
    return this.FeatureRedirector.viewPackage(parent.id);

};

// ------------------------------------------------------------ Private Methods

chorus.ViewPackageController.prototype._loadPackage = function() {
    this.packageId = this._getPackageRouteParam();
    //
    //var me = this;
    //this.PackageService.findById(this.packageId, function(feature) {
    //     me.feature = feature;
    //     me.packageId  = feature.packageId;
    //     me.name = feature.name;
    //     me.body = feature.body;
    // });
};

chorus.ViewPackageController.prototype._getPackageRouteParam = function() {
    if (this.$routeParams.packageId == undefined) {
        LOG.error("Unable to create package: packageId not present");
    }
    return this.$routeParams.packageId;
};
