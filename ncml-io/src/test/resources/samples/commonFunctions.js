load("nashorn:mozilla_compat.js");

function assertCloseTo(actual, expected, tolerance, message) {
	var msg = "Expected " + expected + " +/- " + tolerance + ", but got " + actual;
	if (Math.abs(actual - expected) > tolerance) {
		if (message)
		    msg += ": " + message;
		throw msg;
	}
}

function assertEquals(actual, expected, message) {
    var msg = "Expected " + expected + ", but got " + actual;
	if (message)
	    msg += ": " + message;
	if (expected != actual) {
		throw msg;
	}
}

function assertTrue(value, message) {
    var msg = "Expected true, but got " + value;
    if (value != true) {
        throw message ? msg + ": " + message  : msg;
    }
}

function assertNotNull(value, message) {
    var msg = "Expected something, but got nothing";
    if (value == undefined || value == null) {
        throw message ? msg + ": " + message : msg;
    }
}

function assertNull(value, message) {
    var msg = "Expected nothing, but got " + value;
    if (!(value == undefined || value == null)) {
        throw message ? msg + ": " + message : msg;
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

function createNcArray(dataType, shape, assignmentFunction) {
	var IntArray = Java.type("int[]");
	var jShape = new IntArray(shape.length);
	for (var i = 0; i < shape.length; i++) {
		jShape[i] = shape[i];
	}
    var ncArray = Java.type("ucar.ma2.Array").factory(dataType, jShape);
    for (var it = ncArray.getIndexIterator(); it.hasNext(); ) {
        assignmentFunction(it);
    }
    return ncArray;
}

function random(min, max) {
    var delta = max - min;
    var base = Math.random() * delta;
    return base + min;
}

var scalarIndex = Java.type("ucar.ma2.Index").scalarIndexImmutable;
