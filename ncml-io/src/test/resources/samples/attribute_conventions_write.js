/*-
 * #%L
 * ncml-io
 * %%
 * Copyright (C) 2020 - 2021 Henrique L. F. de Sousa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
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
    
    model.distance = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.DistanceVO(21.4);
    Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfInitializer.initialize(model);

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
	
	var actualDistance = model.distance;
	assertNotNull(actualDistance, "/distance");
	assertNotNull(actualDistance.value, "/distance.value");
	var expectedDistance = netcdf.distance;
	assertCloseTo(expectedDistance.value, actualDistance.value, 0.01, "/distance")
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

    assertEquals(netcdf.unwrap().findDimension("unused_dim").getLength(), 5, "unused_dim dimension in file");
    assertEquals(netcdf.unwrap().findDimension("unlimited_dim").getLength(), 0, "unlimited_dim dimension in file");
}
