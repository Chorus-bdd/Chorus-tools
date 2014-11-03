namespace('chorus');

chorus.FeatureService = function(Restangular) {
    Preconditions.checkNotNull(Restangular, "Restangular must not be null");
    this.Restangular = Restangular;
};

/**
 * Retrieves a feature by its id.
 * @param featureId
 * @param onResultCallback invoked with feature when found
 */
chorus.FeatureService.prototype.findById = function(featureId, onResultCallback) {
    Preconditions.checkNotNull(featureId, "featureId must not be null");
    var URI = "resource/features/" + featureId;
    this.Restangular
        .one(URI).get()
        .then(onResultCallback);
};

chorus.FeatureService.prototype.create = function(featureId, featureText, onResultCallback) {
    var URI = "resource/features/p/" + featureId + "?text=" + featureText;
    this.Restangular
        .one(URI).get()
        .then(onResultCallback);
};

chorus.FeatureService.prototype.update = function(feature, onResultCallback) {
    var URI = "resource/features/p/" + feature.id + "?current-md5=" + feature.md5 + "&text=" + feature.body;
    this.Restangular
        .one(URI).get()
        .then(onResultCallback);
};

chorus.FeatureService.prototype.move = function(featureId, newFeatureId, onResultCallback) {
    var URI = "resource/move-feature/p?target=" + featureId + "&destination=" + newFeatureId;
    this.Restangular
        .one(URI).get()
        .then(onResultCallback);
};

chorus.FeatureService.prototype.delete = function(featureId, onResultCallback) {
    var URI = "resource/features/d/" + featureId;
    this.Restangular
        .one(URI).get()
        .then(onResultCallback);
};


chorus.FeatureService.prototype.toString = function() {
    return "FeatureService";
};


