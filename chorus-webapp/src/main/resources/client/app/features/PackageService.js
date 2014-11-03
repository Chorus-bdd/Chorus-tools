namespace('chorus');

chorus.PackageService = function(Restangular) {
    Preconditions.checkNotNull(Restangular, "Restangular must not be null");
    this.Restangular = Restangular;
};

chorus.PackageService.prototype.findRootSet = function(onResultCallback) {
    var URI = "resource/packages";
    var me = this;
    this.Restangular
        .one(URI).get()
        .then(function(rootPackage) {
            me._loadParentReferences(rootPackage);
            onResultCallback(rootPackage);
        });
};

chorus.PackageService.prototype._loadParentReferences = function(pakage) {
    for (var i in pakage.features) {
        var feature =  pakage.features[i];
        feature.package = pakage;
    }
    for (var i in pakage.subpackages) {
        var subpackage = pakage.subpackages[i];
        subpackage.parent = pakage;
        this._loadParentReferences(subpackage);
    }
};

chorus.PackageService.prototype.parents = function(pakage) {
    var result = [];
    while (pakage.parent) {
        result.push(pakage.parent);
        pakage = pakage.parent;
    }
    return result.reverse();
};




chorus.PackageService.prototype.packageList = function(packageId, callback) {
    var relativeIds = this.toRelativeIds(packageId);
    var result = [];
    //alert( "ids " + JSON.stringify(relativeIds));
    var my = this;
    this.findRootSet(function(rootSet) {
        var lastParent = rootSet;
        //alert("got root set"+ JSON.stringify(rootSet));
        for (var i in relativeIds) {
            var subpackage = my.findSubpackageByRelativeId(lastParent, relativeIds[i]);
            if (subpackage == null) {
                throw {name: "invalid path", message:"could not find subpackage - invalid package path " + relativeIds[i] + " on parent " + lastParent.id};
            }
            result.push(subpackage);
            lastParent = subpackage;
        }
        callback(result);
    });
};



chorus.PackageService.prototype.findById = function(packageId, onResultCallback) {
    Preconditions.checkNotNull(packageId, "packageId must not be null");
    return this.packageList(packageId, function(resultList) {
        onResultCallback(resultList.pop());
    });
    //var URI = "resource/packages/" + packageId;
    //this.Restangular
    //    .one(URI).get()
    //    .then(onResultCallback);
};

chorus.PackageService.prototype.findSubpackageByRelativeId = function(pakage, relativeId) {
    for (var i in pakage.subpackages) {
        var subpackage = pakage.subpackages[i];
        if (subpackage.id == this.toAbsoluteId(pakage.id, relativeId)) {
            return subpackage;
        }
    }
    return null;
};

chorus.PackageService.prototype.ROOT_ID = "";

chorus.PackageService.prototype.toAbsoluteId = function(parentId, relativeId) {
    if (parentId == this.ROOT_ID) {
        return relativeId;
    }
    return parentId + "." + relativeId;
};

chorus.PackageService.prototype.toRelativeIds = function(packageId) {
    return packageId.split(".");
};


/**
 * @param onResultCallback  package->callback
 */
chorus.PackageService.prototype.create = function(packageId, onResultCallback) {
    var URI = "resource/packages/p/" + packageId;
    this.Restangular
        .one(URI).get()
        .then(onResultCallback);
};

/**
 * @param onResultCallback  newPackage->callback
 */
chorus.PackageService.prototype.move = function(packageId, newPackageId, onResultCallback) {
    var URI = "resource/move-package?target=" + packageId + "&destination=" + newPackageId;
    this.Restangular
        .one(URI).get()
        .then(onResultCallback);
};

// TODO: convert success into a boolean callback
chorus.PackageService.prototype.delete = function(packageId, onResultCallback) {
    var URI = "resource/packages/d/" + packageId;
    this.Restangular
        .one(URI).get()
        .then(onResultCallback);
};

chorus.PackageService.prototype.toString = function() {
    return "PackageService";
};


