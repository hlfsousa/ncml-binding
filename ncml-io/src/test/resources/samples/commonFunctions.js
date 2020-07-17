load("nashorn:mozilla_compat.js");

function assertEquals(actual, expected, message) {
    var msg = "Expected true, but got " + actual;
	if (message)
	    msg += ". " + message;
	if (expected != actual) {
		throw msg;
	}
}

function assertTrue(value, message) {
    var msg = "Expected true, but got " + value;
    if (value != true) {
        throw message ? msg + " / " + message  : msg;
    }
}

function assertNotNull(value, message) {
    var msg = "Expected something, but got nothing";
    if (value == undefined || value == null) {
        throw message ? msg : msg + " / " + message;
    }
}

function arrayEquals(a1, a2, debug) {
	if (debug) {
		print(Java.type("java.util.Arrays").toString(a1));
		print(Java.type("java.util.Arrays").toString(a2));
	}
	
    if (a1 == a2)
        return true;
    if (a1.length != a2.length)
        return false;
    for (var idx = 0; idx < a1.length; ++idx) {
        if (a1[idx].class.isArray()) {
            if (!arrayEquals(a1[idx], a2[idx]))
            	return false;
        } else if (a1[idx] != a2[idx]) {
            return false;
		}
    }
    return true;
}

function print() {
    for (var i = 0; i < arguments.length; i++ ) {
       var value = arguments[i];
       java.lang.System.out.print( value );
    }
    java.lang.System.out.println();
}

function printf(format) {
    java.lang.System.out.printf(format, Array.prototype.slice.call(arguments));
}