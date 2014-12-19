namespace('chorus');

chorus.CreateFeatureController = function($routeParams, FeatureService, FeatureRedirector) {
    Preconditions.checkNotNull($routeParams, "$routeParams must not be null");
    Preconditions.checkNotNull(FeatureService, "FeatureService must not be null");
    Preconditions.checkNotNull(FeatureRedirector, "FeatureRedirector must not be null");
    this.redirector = FeatureRedirector;
    this.FeatureService = FeatureService;
    this.$routeParams = $routeParams;

    if (this.$routeParams.packageId == undefined) {
        LOG.error("Unable to create feature: FeatureId not present");
    }
    this.package = this.$routeParams.packageId;
    this.name = "";
    this.body = "";
};

chorus.CreateFeatureController.prototype.save = function() {
    Preconditions.checkArgument(this.name.length, "name must not be empty");
    var featureId = this._getFeatureId();
    var me = this;
    this.FeatureService.create(featureId, this.body, function(result) {
        me.redirector.viewFeature(featureId);
    });

};

chorus.CreateFeatureController.prototype._getFeatureId = function() {
    return this.package + "." + this.name; // TODO: convert name to camel case
};