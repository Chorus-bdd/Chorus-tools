namespace('chorus');

chorus.CreatePackageController = function($routeParams, PackageService, FeatureRedirector) {
    Preconditions.checkNotNull($routeParams, "$routeParams must not be null");
    Preconditions.checkNotNull(PackageService, "PackageService must not be null");
    Preconditions.checkNotNull(FeatureRedirector, "FeatureRedirector must not be null");
    this.FeatureRedirector = FeatureRedirector;
    this.PackageService = PackageService;
    this.$routeParams = $routeParams;

    this.name = "";
    this.packageId = this._getPackageRouteParam();
};

chorus.CreatePackageController.prototype.create = function() {
    Preconditions.checkNotNull(this.packageId, "packageId must not be null");
    Preconditions.checkNotNull(this.name, "name must not be null");
    var me = this;
    var destinationId = this._getDestinationId();
    this.PackageService.create(this._getDestinationId(), function(result) {
        // todo: check result
        me.FeatureRedirector.viewPackage(destinationId);
    });
};

chorus.CreatePackageController.prototype.cancel = function() {
    this.FeatureRedirector.viewPackage(this.packageId);
};

// ------------------------------------------------------------ Private Methods

chorus.CreatePackageController.prototype._getPackageRouteParam = function() {
    if (this.$routeParams.packageId == undefined) {
        LOG.error("Unable to create package: packageId not present");
    }
    return this.$routeParams.packageId;
};


chorus.CreatePackageController.prototype._getDestinationId = function() {
    return this.packageId + "." + this.name; // TODO: convert name to camel case
};
