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
importPackage(Packages.ucar.ma2);

var IntArray = Java.type("int[]");
var IntArray2 = Java.type("int[][]");

function createModel(model) {
    model = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO();

    Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfInitializer.initialize(model);
    assertEquals(model.title, "Mapped Properties Test");

    var group = new Packages.io.github.hlfsousa.ncml.io.test.GroupMapVO();
    group.name = "g01";
    group.items = new Packages.io.github.hlfsousa.ncml.io.test.GroupMapVO.ItemsVO();

    var numberOfItems = 20; // dimension number_of_items, declared length is 10

    group.items.value = function() {
	    var value = new IntArray(numberOfItems);
        for (var i = 0; i < numberOfItems; i++) {
            value[i] = Math.round(random(0, 100));
        }
        return value;
    }();

    model.groupMap = new java.util.LinkedHashMap();
    model.groupMap.put("g01", group);

    var maxTemp = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.TemperatureMapVO();
    maxTemp.longName = "maximum temperature";
    Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfInitializer.TemperatureMapInitializer.initialize(maxTemp);
    assertEquals(maxTemp.units, "Celsius degrees");
    maxTemp.value = random(0, 35);
	
    var minTemp = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.TemperatureMapVO();
    minTemp.longName = "minimum temperature";
    minTemp.value = random(0, 35);

    var temperatureMap = new java.util.LinkedHashMap();
    temperatureMap.put("temp_max", maxTemp);
    temperatureMap.put("temp_min", minTemp);
    model.temperatureMap = temperatureMap;

    model.intMap = new java.util.LinkedHashMap();
    model.intMap.put("int_A", 1);
    model.intMap.put("int_B", 2);

    /* Variable length dimensions are not working. We're writing them the same way they seem to be represented in any
     * value that we read using netcdf-java: the variable length is represented by individual arrays of the declared
     * data type, and they are added to a fixed-dimension array of type object. The value is written to a variable of
     * the declared data type, and the variable length dimension is expressed as an asterisk in the variable shape 
     * without a shared dimension or type declaration in the group:
     * 
     *   dimensions:
     *     number_of_items = 20;
     *   variables:
     *     int varLength(number_of_items=20, *);
     * 
     * With this setup, we get an HDF error when trying to write the first variable, whatever it is.
     * In 2016, we find this: https://www.unidata.ucar.edu/mailing_lists/archives/netcdf-java/2016/msg00115.html
     * "In a future release of the netcdf java code I plan to get rid of the variable-length dimension (e..g '*') in CDM
     * and replace it with ArraySequence."
     * See also https://www.unidata.ucar.edu/software/netcdf-java/v4.6/reference/StructureData.html
     */
    /**/
    model.varLength = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.VarLengthVO();
    model.varLength.value = function() {
	    var outerArray = new IntArray2(numberOfItems);
        for (var i = 0; i < numberOfItems; i++) {
	        var innerLength = Math.round(random(10, 20));
            var innerArray = new IntArray(innerLength);
            for (var j = 0; j < innerLength; j++) {
	            innerArray[j] = Math.round(random(1000, 5000));
            }
            outerArray[i] = innerArray;
        }
        return outerArray;
    }();
    /**/

    Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfInitializer.initialize(model);
    assertEquals(group.items.hasDefaultValue, "set by initializer", "/groupMap[g01]/items/has_default_value");

    return model;
}

function verifyCreatedFile(netcdf, model) {
    var expectedGroupMap = model.groupMap;
    var actualGroupMap = netcdf.groupMap;
    assertNotNull(actualGroupMap, "/groupMap");

    assertEquals(netcdf.title, model.title, "/@title");

    for (var key in expectedGroupMap) {
        var groupObj = actualGroupMap[key];
        assertNotNull(groupObj, "/groupMap[" + key + "]");
        assertEquals(groupObj.name, key, "/groupMap[" + key + "]/name");
        var actualItems = groupObj.items.value;
        assertNotNull(actualItems, "/groupMap[" + key + "]/items/value")
    }
    
    var expectedVarMap = model.temperatureMap;
    var actualVarMap = netcdf.temperatureMap;
    assertNotNull(actualVarMap, "/temperatureMap");
    for (var key in expectedVarMap) {
        var actualVar = actualVarMap[key];
        assertNotNull(actualVar, "/temperatureMap[" + key + "]")
        var expectedVar = expectedVarMap[key]
        assertEquals(actualVar.longName, expectedVar.longName, "/temperatureMap[" + key + "].longName");
        assertTrue(arrayEquals(actualVar.value, expectedVar.value), "/temperatureMap[" + key + "].value");
        assertEquals(actualVar.units, expectedVar.units, "temperatureMap[" + key + "].units");
    }

    var expectedIntMap = model.intMap;
    var actualIntMap = netcdf.intMap;
    for (var key in expectedIntMap) {
	    assertNotNull(actualIntMap[key]);
        assertEquals(actualIntMap[key], expectedIntMap[key], key);
	}

    /**/
    var expectedVarLength = model.varLength;
    var actualVarLength = netcdf.varLength;
    assertNotNull(actualVarLength, "/varLength");
    assertTrue(arrayEquals(expectedVarLength.value, actualVarLength.value, true), "/varLength content");
    /**/
}

function editModel(netcdf) {
    var group = new Packages.io.github.hlfsousa.ncml.io.test.GroupMapVO();
    group.name = "g02";
    var numberOfItems = 20;
    group.items = new Packages.io.github.hlfsousa.ncml.io.test.GroupMapVO.ItemsVO();
    group.items.value = function(){
	    var value = new IntArray(numberOfItems);
        for (var i = 0; i < numberOfItems; i++) {
	        value[i] = Math.round(random(0, 100));
        }
        return value;
    }();
    netcdf.groupMap["g02"] = group;

    var avgTemp = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.TemperatureMapVO();
    avgTemp.longName = "average temperature";
    avgTemp.value = random(10, 20);
    netcdf.temperatureMap["temp_average"] = avgTemp;

    return netcdf;
}

function verifyEditedFile(netcdf, model) {
    verifyCreatedFile(netcdf, model);
}
