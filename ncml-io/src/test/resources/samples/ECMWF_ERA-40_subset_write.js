importPackage(Packages.io.github.hlfsousa.ncml.io.test);
importPackage(Packages.ucar.ma2);

var FloatArray = Java.type("float[]");
var IntArray = Java.type("int[]");

function random(min, max) {
	var delta = max - min;
	var base = Math.random() * delta;
	return base + min;
}

function createModel(model) {
	if (!model.longitude) {
		var longitude = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.LongitudeVO();
		model.longitude = longitude;
	}
	assertNotNull(model.longitude, "empty/longitude"); // interface, let handler create
	var shape = new IntArray(1);
	shape[0] = 144;
	var longitude = new FloatArray(144);
	for (var i = 0; i < shape[0]; i++) {
		longitude[i] = random(0, 360);
	}
	model.longitude.value = longitude;
	return model;
}

function verifyCreatedFile(netcdf, model) {
	assertNotNull(netcdf, "no file to check");
	var expectedLongitude = model.longitude;
	var actualLongitude = netcdf.longitude;
	assertNotNull(actualLongitude, "/longitude");
	var expectedLonValue = expectedLongitude.value;
	var actualLonValue = actualLongitude.value;
	assertTrue(arrayEquals(expectedLonValue, actualLonValue), "/longitude/value")
}

function editModel(netcdf) {
	assertNotNull(netcdf, "no file to modify");
	var IntArray = Java.type("int[]");
	var shape = new IntArray(1);
	shape[0] = 73;
	var latitude = new FloatArray(shape[0]);
    for (var i = 0; i < shape[0]; i++) {
		latitude[i] = random(0, 360);
    }
	netcdf.latitude.value = latitude;
	return netcdf;
}

function verifyEditedFile(netcdf, model) {
	verifyCreatedFile(netcdf, model);
	
	//assertTrue(model.longitude.value === netcdf.longitude.value, "longitude sameness");
	//assertTrue(model.longitude.hashCode() == netcdf.longitude.hashCode(), "longitude hash");
	
	assertNotNull(netcdf, "no file to check");
	var expectedLatitude = model.latitude;
	var actualLatitude = netcdf.latitude;
	assertNotNull(actualLatitude, "/latitude");
	var expectedLonValue = expectedLatitude.value;
	var actualLonValue = actualLatitude.value;
	assertTrue(arrayEquals(expectedLonValue, actualLonValue), "/latitude/value (shape)")
}