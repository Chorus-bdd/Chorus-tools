
var Preconditions = new function() {

    this.checkArgument = function(value, msg) {
        if (!value) {
            throwException(msg || "argument check failed");
        }
    };

    this.checkNotNull = function(value, msg) {
        if (value !== undefined && value != null) {
            return;
        }
        throwException(msg || "argument must not be null");
    };

    var throwException = function(msg) {
        throw {
            name: "Precondition Check Failed",
            message: msg,
            toString: function () {
                return this.name + ": " + this.message
            }
        }
    }
}