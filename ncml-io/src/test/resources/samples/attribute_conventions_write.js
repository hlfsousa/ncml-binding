importPackage(Packages.io.github.hlfsousa.ncml.io.test);

var IntArray = Java.type("int[]");
var FloatArray = Java.type("float[]");
var DoubleArray = Java.type("double[]");

function createModel(model) {
    model = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO();
    model.temperature = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.TemperatureVO();
	model.temperature.scaleFactor = 0.25;
	model.temperature.addOffset = 15;
	model.temperature.dimensions = [ new Packages.ucar.nc2.Dimension("number_of_items", 20) ];
    model.temperature.value = new DoubleArray(20);
    for (var i = 0; i < 20; i++) {
	    model.temperature.value[i] = random(5, 42);
    }
    return model;
}

function verifyCreatedFile(netcdf, model) {
    var actualTemperature = model.temperature;
    assertNotNull(actualTemperature, "/temperature")
	assertNotNull(actualTemperature.value, "/temperature.value")

    var expectedTemperature = netcdf.temperature;
    assertNotNull(expectedTemperature, "/temperature (file)")
	assertNotNull(expectedTemperature.value, "/temperature.value (file)")

	for (var idx = 0; idx < actualTemperature.value.length; idx++) {
		var actualValue = actualTemperature.value[idx];
		var expectedValue = expectedTemperature.value[idx];
		assertCloseTo(actualValue, expectedValue, 0.25, "/temperature.value[" + idx + "]")
	}
}

function editModel(netcdf) {
    netcdf.temperature = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.TemperatureVO();
	netcdf.temperature.scaleFactor = 0.25;
	netcdf.temperature.addOffset = 15;
	netcdf.temperature.dimensions = [ new Packages.ucar.nc2.Dimension("number_of_items", 15) ];
    netcdf.temperature.value = new DoubleArray(15);
    for (var i = 0; i < 15; i++) {
	    netcdf.temperature.value[i] = random(5, 42);
    }
    return netcdf;
}

function verifyEditedFile(netcdf, model) {
    verifyCreatedFile(netcdf, model);
}