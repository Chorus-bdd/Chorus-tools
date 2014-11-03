
var Log = new function() {
    var TRACE_ENABLED = true;
    var DEBUG_ENABLED = true;
    var INFO_ENABLED  = true;
    var WARN_ENABLED  = true;
    var ALERT_ENABLED = true;

    this.trace = function(out) {
        if (platformHasConsole() && TRACE_ENABLED) {
            console.log(out);
        }
    };

    this.debug = function(out) {
        if (platformHasConsole() && DEBUG_ENABLED) {
            console.log(out);
        }
    };

    this.info = function(out) {
        if (platformHasConsole() && INFO_ENABLED) {
            console.log(out);
        }
    };

    this.warn = function(out) {
        if (platformHasConsole() && WARN_ENABLED) {
            console.log(out);
        }
    }

    function alert(out) {
        if (ALERT_ENABLED) {
            alert(out);
        }
    };

    var platformHasConsole = function() {
        return window.console && console.log;
    }
}