importPackage(Packages.hsousa.ncml.io.test);
importPackage(Packages.ucar.ma2);

function createModel(model) {
    model = new Packages.hsousa.ncml.io.test.TestNetcdfVO();
    model.temperature = new Packages.hsousa.ncml.io.test.TestNetcdfVO.TemperatureVO();
	model.temperature.scaleFactor = 0.25;
	model.temperature.addOffset = 15;
	model.temperature.dimensions = [ new Packages.ucar.nc2.Dimension("number_of_items", 20) ];
    model.temperature.value = createNcArray(Java.type("ucar.ma2.DataType").DOUBLE, [20], function(it) {
        it.setDoubleNext(random(5, 42));
    });
    return model;
}

function verifyCreatedFile(netcdf, model) {
	var line = 0;
    var actualTemperature = model.temperature;
    assertNotNull(actualTemperature, "/temperature")
	assertNotNull(actualTemperature.value, "/temperature.value")
	assertNotNull(actualTemperature.value.storage, "/temperature.value.storage")

    var expectedTemperature = netcdf.temperature;
    assertNotNull(expectedTemperature, "/temperature (file)")
	assertNotNull(expectedTemperature.value, "/temperature.value (file)")
	assertNotNull(expectedTemperature.value.storage, "/temperature.value.storage (file)")

	for (var idx = 0; idx < actualTemperature.value.storage.length; idx++) {
		var actualValue = actualTemperature.value.storage[idx];
		var expectedValue = expectedTemperature.value.storage[idx];
		assertCloseTo(actualValue, expectedValue, 0.25, "/temperature.value[" + idx + "]")
	}
}

function editModel(netcdf) {
    netcdf.temperature = new Packages.hsousa.ncml.io.test.TestNetcdfVO.TemperatureVO();
	netcdf.temperature.scaleFactor = 0.25;
	netcdf.temperature.addOffset = 15;
	netcdf.temperature.dimensions = [ new Packages.ucar.nc2.Dimension("number_of_items", 15) ];
    netcdf.temperature.value = createNcArray(Java.type("ucar.ma2.DataType").DOUBLE, [15], function(it) {
        it.setDoubleNext(random(5, 42));
    });
    return netcdf;
}

function verifyEditedFile(netcdf, model) {
    verifyCreatedFile(netcdf, model);
}