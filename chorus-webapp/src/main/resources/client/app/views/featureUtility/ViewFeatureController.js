namespace('chorus');

chorus.ViewFeatureController = function($routeParams,PackageService, FeatureService, FeatureRedirector) {
    Preconditions.checkNotNull($routeParams, "$routeParams must not be null");
    Preconditions.checkNotNull(PackageService, "PackageService must not be null");
    Preconditions.checkNotNull(FeatureService, "FeatureService must not be null");
    Preconditions.checkNotNull(FeatureRedirector, "FeatureRedirector must not be null");
    this.FeatureRedirector = FeatureRedirector;
    this.PackageService = PackageService;
    this.FeatureService = FeatureService;
    this.$routeParams = $routeParams;

    this.featureId = "";
    this.packageId = "";
    this.name = "";
    this.body = "";

    this._loadFeature();
};

chorus.ViewFeatureController.prototype.edit = function() {
    this.FeatureRedirector.editFeature(this.featureId);
};

chorus.ViewFeatureController.prototype.move = function() {
    this.FeatureRedirector.moveFeature(this.featureId);
};

chorus.ViewFeatureController.prototype.delete = function() {
    this.FeatureService.delete(this.featureId);
};


// common
chorus.ViewFeatureController.prototype.viewPackage = function(packageId) {
    return this.FeatureRedirector.viewPackage(packageId);
};

// ------------------------------------------------------------ Private Methods

chorus.ViewFeatureController.prototype._loadFeature = function() {
    this.featureId = this._getFeatureRouteParam();

    var me = this;
    this.FeatureService.findById(this.featureId, function(feature) {
        me.feature = feature;
        me.packageId  = feature.packageId;
        me.name = feature.name;
        me.body = feature.body;
        me._loadPackage();
    });
};

chorus.ViewFeatureController.prototype._loadPackage = function() {
    this.featureId = this._getFeatureRouteParam();

    var me = this;
    this.PackageService.findById(this.packageId, function(pakage) {
        //$scope.$apply(function() {
        me.package = pakage;
        me.parentPackages = me.PackageService.parents(me.package);
        me.parentPackages.push(me.package);
        //});
    });
};


chorus.ViewFeatureController.prototype._getFeatureRouteParam = function() {
    if (this.$routeParams.featureId == undefined) {
        LOG.error("Unable to create feature: FeatureId not present");
    }
    return this.$routeParams.featureId;
};

