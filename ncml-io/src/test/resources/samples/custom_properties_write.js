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
var StringArray = Java.type("java.lang.String[]");

function createModel(model) {
    model = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO();
    var group = new Packages.io.github.hlfsousa.ncml.io.test.MyGroupVO();
    group.name = "group_01";
    group.groupItems = new Packages.io.github.hlfsousa.ncml.io.test.MyGroupVO.ItemsVO();
    group.groupItems.value = function() {
	    var value = new IntArray(10);
        for (var i = 0; i < 10; i++) {
	        value[i] = Math.round(random(0, 100));
        }
        return value;
    }();
    group.groupItems.setMyVariableAttribute("custom attribute - g01");

    model.mappedGroup = new java.util.LinkedHashMap();
    model.mappedGroup.put("group_01", group);

    var maxTemp = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.TemperaturesVO();
    maxTemp.setLongName("maximum temperature");
    maxTemp.value = random(0, 35);
    var minTemp = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.TemperaturesVO();
    minTemp.setLongName("minimum temperature");
    minTemp.value = random(0, 35);
    var temperatures = new java.util.LinkedHashMap();
    temperatures.put("temp_max", maxTemp);
    temperatures.put("temp_min", minTemp);
    model.temperatures = temperatures;

    var someGroup = new Packages.io.github.hlfsousa.ncml.io.test.SomeGroupVO();
    someGroup.someProperty = new Packages.io.github.hlfsousa.ncml.io.test.SomeGroupVO.SomePropertyVO();
    someGroup.someProperty.value = function(){
	    var value = new IntArray(10);
        for (var i = 0; i < 10; i++) {
	        value[i] = Math.round(random(0, 100));
        }
        return value;
    }();
    someGroup.someProperty.longName = "Property name was substituted :)";
    model.someGroup = someGroup;
    model.myGlobalAttribute = "global attribute customization ok";

    model.someString = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.SomeStringVO();
    model.someString.value = "this is a string";

    model.stringArray = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.StringArrayVO();
    model.stringArray.value = function() {
	    var value = new StringArray(10);
        for (var i = 0; i < 10; i++) {
	        value[i] = "str_" + i;
        }
        return value;
    }();

    return model;
}

function verifyCreatedFile(netcdf, model, lowLevelCheck) {
    var expectedGroupMap = model.mappedGroup;
    var actualGroupMap = netcdf.mappedGroup;
    assertNotNull(actualGroupMap, "/mappedGroup");
    for (key in expectedGroupMap) {
        var groupObj = actualGroupMap[key];
        assertNotNull(groupObj, "/mappedGroup[" + key + "]");
        assertEquals(groupObj.name, key, "/mappedGroup[" + key + "]/name");
        var actualItems = groupObj.groupItems.value;
        assertNotNull(actualItems, "/mappedGroup[" + key + "]/items/value");
        if (lowLevelCheck) {
            assertEquals(groupObj.unwrap().shortName, key, "unwrapped group name");

            assertNull(groupObj.unwrap().findVariable("items"), "items not renamed at runtime");
            assertNotNull(groupObj.unwrap().findVariable("components"), "items not renamed at runtime");

            assertNotNull(groupObj.unwrap().findVariable("components").findAttribute("my_attribute"),
                    "unwraped variable attribute my_attribute at /mappedGroup[" + key + "]/items");
        }
    }
    
    var expectedVarMap = model.temperatures;
    var actualVarMap = netcdf.temperatures;
    assertNotNull(actualVarMap, "/temperatures");
    for (key in expectedVarMap) {
        var actualVar = actualVarMap[key];
        assertNotNull(actualVar, "/temperatures[" + key + "]")
        var expectedVar = expectedVarMap[key]
        assertEquals(actualVar.longName, expectedVar.longName, "/temperatures[" + key + "].longName");
        assertTrue(arrayEquals(actualVar.value, expectedVar.value), "/temperatures[" + key + "].value")
        assertEquals(actualVar.customVariableProperty, expectedVar.customVariableProperty);
    }

    assertEquals(netcdf.someGroup.someProperty.longName, netcdf.someGroup.someProperty.longName, "/someGroup/someProperty/@long_name");
    assertEquals(netcdf.myGlobalAttribute, netcdf.myGlobalAttribute);

    if (lowLevelCheck) {
        assertEquals(netcdf.someGroup.unwrap().shortName, "better_group_name", "unwrapped/replaced group name");
        assertNull(netcdf.unwrap().findAttribute("customglobalattribute"), "global attribute not renamed by properties");
        assertNotNull(netcdf.unwrap().findAttribute("title"), "global attribute not renamed by properties");
    }

    assertNotNull(netcdf.someString, "/someString empty");
    assertEquals(netcdf.someString.value, model.someString.value, "/someString");
    var expectedStringArray = model.stringArray;
    var actualStringArray = netcdf.stringArray;
    assertNotNull(actualStringArray, "/stringArray");
    assertNotNull(actualStringArray.value, "/stringArray.value");
    assertTrue(arrayEquals(expectedStringArray.value, actualStringArray.value), "/stringArray.value");
}

function editModel(netcdf) {
    var group = new Packages.io.github.hlfsousa.ncml.io.test.MyGroupVO();
    group.name = "group_02";
    var IntArray = Java.type("int[]");
    group.groupItems = new Packages.io.github.hlfsousa.ncml.io.test.MyGroupVO.ItemsVO();
    group.groupItems.value = function(){
	    var value = new IntArray(10);
        for (var i = 0; i < 10; i++) {
	        value[i] = Math.round(random(0, 100));
        }
        return value;
    }();
    group.groupItems.myVariableAttribute = "custom attribute - g02";
    netcdf.mappedGroup["group_02"] = group;

    var avgTemp = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.TemperaturesVO();
    avgTemp.longName = "average temperature";
    avgTemp.value = random(10, 20);
    netcdf.temperatures["temp_average"] = avgTemp;

    return netcdf;
}

function verifyEditedFile(netcdf, model) {
    verifyCreatedFile(netcdf, model, true);
}
