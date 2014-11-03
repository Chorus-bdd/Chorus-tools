namespace('chorus');

chorus.MovePackageController = function($routeParams, PackageService, FeatureRedirector) {
    Preconditions.checkNotNull($routeParams, "$routeParams must not be null");
    Preconditions.checkNotNull(PackageService, "PackageService must not be null");
    Preconditions.checkNotNull(FeatureRedirector, "FeatureRedirector must not be null");
    this.redirector = FeatureRedirector;
    this.PackageService = PackageService;
    this.$routeParams = $routeParams;

    this.packageId = this._getPackageRouteParam();
    this.destinationId = "";
};

chorus.MovePackageController.prototype.move = function() {
    Preconditions.checkNotNull(this.packageId, "packageId must not be null");
    var me = this;
    this.PackageService.move(this.packageId, this.destinationId, function(result) {
        // todo: check result
        me.redirector.viewPackage(me.destinationId);
    });
};

chorus.MovePackageController.prototype.cancel = function() {
    this.redirector.viewPackage(this.packageId);
};

// ------------------------------------------------------------ Private Methods

chorus.MovePackageController.prototype._getPackageRouteParam = function() {
    if (this.$routeParams.packageId == undefined) {
        LOG.error("Unable to create package: packageId not present");
    }
    return this.$routeParams.packageId;
};
