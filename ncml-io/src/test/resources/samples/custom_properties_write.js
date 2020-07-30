importPackage(Packages.hsousa.ncml.io.test);
importPackage(Packages.ucar.ma2);

var scalarIndex = Java.type("ucar.ma2.Index").scalarIndexImmutable;

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

function createModel(model) {
    model = new Packages.hsousa.ncml.io.test.TestNetcdfVO();
    var group = new Packages.hsousa.ncml.io.test.MappedGroupVO();
    group.name = "g01";
    group.groupItems = new Packages.hsousa.ncml.io.test.MappedGroupVO.GroupItemsVO();
    group.groupItems.value = createNcArray(Java.type("ucar.ma2.DataType").INT, [10], function(it) {
        it.setIntNext(Math.round(random(0, 100)));
    });
    model.mappedGroup = new java.util.LinkedHashMap();
    model.mappedGroup.put("g01", group);

    var maxTemp = new Packages.hsousa.ncml.io.test.TestNetcdfVO.TemperaturesVO();
    maxTemp.setLongName("maximum temperature");
    maxTemp.setValue(createNcArray(Java.type("ucar.ma2.DataType").FLOAT, [], function(it) {
        it.setDoubleNext(random(0, 35));
    }).getObject(scalarIndex));
    maxTemp.myVariableAttribute = "custom attribute - max temperature";
    var minTemp = new Packages.hsousa.ncml.io.test.TestNetcdfVO.TemperaturesVO();
    minTemp.setLongName("minimum temperature");
    minTemp.setValue(createNcArray(Java.type("ucar.ma2.DataType").FLOAT, [], function(it) {
        it.setDoubleNext(random(0, 35));
    }).getObject(scalarIndex));
    minTemp.myVariableAttribute = "custom attribute - min temperature";
    var temperatures = new java.util.LinkedHashMap();
    temperatures.put("temp_max", maxTemp);
    temperatures.put("temp_min", minTemp);
    model.temperatures = temperatures;

    var someGroup = new Packages.hsousa.ncml.io.test.SomeGroupVO();
    someGroup.someProperty = new Packages.hsousa.ncml.io.test.SomeGroupVO.SomePropertyVO();
    someGroup.someProperty.value = createNcArray(Java.type("ucar.ma2.DataType").INT, [10], function(it) {
        it.setIntNext(Math.round(random(0, 100)));
    });
    someGroup.someProperty.longName = "Property name was substituted :)";
    model.someGroup = someGroup;
    model.myGlobalAttribute = "global attribute customization ok";

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
            assertNotNull(groupObj.unwrap().findVariable("items").findAttribute("my_attribute"),
                    "unwraped variable attribute my_attribute");
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
        assertTrue(arrayEquals(actualVar.value.storage, expectedVar.value.storage), "/temperatures[" + key + "].value")
        assertEquals(actualVar.customVariableProperty, expectedVar.customVariableProperty);
    }

    assertEquals(netcdf.someGroup.someProperty.longName, netcdf.someGroup.someProperty.longName, "/someGroup/someProperty/@long_name");
    assertEquals(netcdf.myGlobalAttribute, netcdf.myGlobalAttribute);

    if (lowLevelCheck) {
        assertEquals(netcdf.someGroup.unwrap().shortName, "this_group_name_is_not_good_for_a_property", "unwrapped group name");
        assertNotNull(netcdf.unwrap().findAttribute("customglobalattribute"), "unwrapped global attribute");
    }

}

function editModel(netcdf) {
    var group = new Packages.hsousa.ncml.io.test.MappedGroupVO();
    group.name = "g02";
    var IntArray = Java.type("int[]");
    var shape = new IntArray(1);
    shape[0] = 10;
    var ncArray = Java.type("ucar.ma2.Array").factory(Java.type("ucar.ma2.DataType").INT, shape);
    for (var it = ncArray.getIndexIterator(); it.hasNext(); ) {
        it.setIntNext(Math.round(random(0, 100)));
    }
    group.groupItems = new Packages.hsousa.ncml.io.test.MappedGroupVO.GroupItemsVO();
    group.groupItems.value = ncArray;
    netcdf.mappedGroup["g02"] = group;

    var avgTemp = new Packages.hsousa.ncml.io.test.TestNetcdfVO.TemperaturesVO();
    avgTemp.longName = "average temperature";
    avgTemp.setValue(createNcArray(Java.type("ucar.ma2.DataType").FLOAT, [], function(it) {
        it.setDoubleNext(random(10, 20));
    }).getObject(scalarIndex));
    netcdf.temperatures["temp_average"] = avgTemp;

    return netcdf;
}

function verifyEditedFile(netcdf, model) {
    verifyCreatedFile(netcdf, model, true);
}
