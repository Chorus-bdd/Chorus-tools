namespace('chorus');

chorus.EditFeatureController = function($routeParams, FeatureService, FeatureRedirector) {
    Preconditions.checkNotNull($routeParams, "$routeParams must not be null");
    Preconditions.checkNotNull(FeatureService, "FeatureService must not be null");
    Preconditions.checkNotNull(FeatureRedirector, "FeatureRedirector must not be null");
    this.redirector = FeatureRedirector;
    this.FeatureService = FeatureService;
    this.$routeParams = $routeParams;

    this.featureId = "";
    this.packageId = "";
    this.feature = null;
    this.name = "";
    this.body = "";

    this._loadFeature();
};

chorus.EditFeatureController.prototype.save = function() {
    Preconditions.checkNotNull(this.featureId, "featureId must not be null");
    this.feature.body = this.body;
    var me = this;
    this.FeatureService.update(this.feature, function(result) {
        me.redirector.viewFeature(me.featureId);
    });
};

chorus.EditFeatureController.prototype.cancel = function() {
    this.redirector.viewFeature(this.featureId);
};

// ------------------------------------------------------------ Private Methods

chorus.EditFeatureController.prototype._loadFeature = function() {
    this.featureId = this._getFeatureRouteParam();

    var me = this;
    this.FeatureService.findById(this.featureId, function(feature) {
         me.feature = feature;
         me.packageId  = feature.packageId;
         me.name = feature.name;
         me.body = feature.body;
     });
};

chorus.EditFeatureController.prototype._getFeatureRouteParam = function() {
    if (this.$routeParams.featureId == undefined) {
        LOG.error("Unable to create feature: FeatureId not present");
    }
    return this.$routeParams.featureId;
};
