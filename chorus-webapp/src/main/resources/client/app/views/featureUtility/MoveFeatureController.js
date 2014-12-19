namespace('chorus');

chorus.MoveFeatureController = function($routeParams, FeatureService, FeatureRedirector) {
    Preconditions.checkNotNull($routeParams, "$routeParams must not be null");
    Preconditions.checkNotNull(FeatureService, "FeatureService must not be null");
    Preconditions.checkNotNull(FeatureRedirector, "FeatureRedirector must not be null");
    this.redirector = FeatureRedirector;
    this.FeatureService = FeatureService;
    this.$routeParams = $routeParams;

    this.featureId = this._getFeatureRouteParam();
    this.destinationId = "";
};

chorus.MoveFeatureController.prototype.move = function() {
    Preconditions.checkNotNull(this.featureId, "featureId must not be null");
    var me = this;
    this.FeatureService.move(this.featureId, this.destinationId, function(result) {
        // todo: check result
        me.redirector.viewFeature(me.destinationId);
    });
};

chorus.MoveFeatureController.prototype.cancel = function() {
    this.redirector.viewFeature(this.featureId);
};

// ------------------------------------------------------------ Private Methods

chorus.MoveFeatureController.prototype._getFeatureRouteParam = function() {
    if (this.$routeParams.featureId == undefined) {
        LOG.error("Unable to create feature: FeatureId not present");
    }
    return this.$routeParams.featureId;
};
