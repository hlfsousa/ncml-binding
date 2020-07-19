importPackage(Packages.hsousa.ncml.io.test);
importPackage(Packages.ucar.ma2);

function random(min, max) {
	var delta = max - min;
	var base = Math.random() * delta;
	return base + min;
}

function createModel(model) {
	print(model.class.classLoader);
	if (!model.longitude) {
		var LongitudeVO = Java.type("hsousa.ncml.io.test.TestNetcdfVO.LongitudeVO");
		print(LongitudeVO);
		print(LongitudeVO.classLoader);
		var longitude = LongitudeVO.create();
		print(longitude);
		model.longitude = longitude;
	}
	assertNotNull(model.longitude, "empty/longitude"); // interface, let handler create
	var IntArray = Java.type("int[]");
	var shape = new IntArray(1);
	shape[0] = 144;
	var ncArray = Java.type("ucar.ma2.Array").factory(Java.type("ucar.ma2.DataType").FLOAT, shape);
    for (var it = ncArray.getIndexIterator(); it.hasNext(); ) {
		it.setFloatNext(random(0, 360));
    }
	model.longitude.value = ncArray;
	return model;
}

function verifyCreatedFile(netcdf, model) {
	assertNotNull(netcdf, "no file to check");
	var expectedLongitude = model.longitude;
	var actualLongitude = netcdf.longitude;
	assertNotNull(actualLongitude, "/longitude");
	var expectedLonValue = expectedLongitude.value;
	var actualLonValue = actualLongitude.value;
	assertTrue(arrayEquals(expectedLonValue.shape, actualLonValue.shape), "/longitude/value (shape)")
	assertTrue(arrayEquals(expectedLonValue.storage, actualLonValue.storage), "/longitude/value (storage)");
}

function editModel(netcdf) {
	assertNotNull(netcdf, "no file to modify");
	var IntArray = Java.type("int[]");
	var shape = new IntArray(1);
	shape[0] = 73;
	var ncArray = Java.type("ucar.ma2.Array").factory(Java.type("ucar.ma2.DataType").FLOAT, shape);
    for (var it = ncArray.getIndexIterator(); it.hasNext(); ) {
		it.setFloatNext(random(0, 360));
    }
	netcdf.latitude.value = ncArray;
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
	assertTrue(arrayEquals(expectedLonValue.shape, actualLonValue.shape), "/latitude/value (shape)")
	assertTrue(arrayEquals(expectedLonValue.storage, actualLonValue.storage), "/latitude/value (storage)");
}