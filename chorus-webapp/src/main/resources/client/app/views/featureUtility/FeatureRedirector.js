
FeatureRedirector = function($location) {
    Preconditions.checkNotNull($location, "$location must not be null");
    this.$location = $location;
};

// ------------------------------------------------------------------- Features

FeatureRedirector.prototype.newFeature = function(packageId) {
    Preconditions.checkNotNull(packageId, "packageId must not be null");
    this.$location.path("/Package/" + packageId + "/createFeature");
};


FeatureRedirector.prototype.viewFeature = function(featureId) {
    Preconditions.checkNotNull(featureId, "featureId must not be null");
    this.$location.path("/Feature/" + featureId);
};


FeatureRedirector.prototype.editFeature = function(featureId) {
    Preconditions.checkNotNull(featureId, "featureId must not be null");
    this.$location.path("/Feature/" + featureId + "/edit");
};

FeatureRedirector.prototype.moveFeature = function(featureId) {
    Preconditions.checkNotNull(featureId, "featureId must not be null");
    this.$location.path("/Feature/" + featureId + "/move");
};

// ------------------------------------------------------------------- Packages

FeatureRedirector.prototype.viewPackage = function(packageId) {
    Preconditions.checkNotNull(packageId, "packageId must not be null");
    //alert("redirecting to ")
    this.$location.path("/Package/" + packageId);
};

FeatureRedirector.prototype.pathToViewPackage = function(packageId) {
    Preconditions.checkNotNull(packageId, "featureId must not be null");
    return "/Package/" + packageId;
};

FeatureRedirector.prototype.newPackage = function(packageId) {
    Preconditions.checkNotNull(packageId, "featureId must not be null");
    this.$location.path("/Package/" + packageId + "/create");
};

FeatureRedirector.prototype.movePackage = function(packageId) {
    Preconditions.checkNotNull(packageId, "featureId must not be null");
    this.$location.path("/Package/" + packageId + "/move");
};
