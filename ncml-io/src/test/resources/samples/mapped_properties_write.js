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
    var group = new Packages.hsousa.ncml.io.test.GroupMapVO();
    group.name = "g01";
    group.items = new Packages.hsousa.ncml.io.test.GroupMapVO.ItemsVO();
    group.items.value = createNcArray(Java.type("ucar.ma2.DataType").INT, [10], function(it) {
        it.setIntNext(Math.round(random(0, 100)));
    });
    model.groupMap = new java.util.LinkedHashMap();
    model.groupMap.put("g01", group);

    var maxTemp = new Packages.hsousa.ncml.io.test.TestNetcdfVO.TemperatureMapVO();
    maxTemp.setLongName("maximum temperature");
    maxTemp.setValue(createNcArray(Java.type("ucar.ma2.DataType").FLOAT, [], function(it) {
        it.setDoubleNext(random(0, 35));
    }).getObject(scalarIndex));
    var minTemp = new Packages.hsousa.ncml.io.test.TestNetcdfVO.TemperatureMapVO();
    minTemp.setLongName("minimum temperature");
    minTemp.setValue(createNcArray(Java.type("ucar.ma2.DataType").FLOAT, [], function(it) {
        it.setDoubleNext(random(0, 35));
    }).getObject(scalarIndex));
    var temperatureMap = new java.util.LinkedHashMap();
    temperatureMap.put("temp_max", maxTemp);
    temperatureMap.put("temp_min", minTemp);
    model.setTemperatureMap(temperatureMap);
    return model;
}

function verifyCreatedFile(netcdf, model) {
    var expectedGroupMap = model.groupMap;
    var actualGroupMap = netcdf.groupMap;
    assertNotNull(actualGroupMap, "/groupMap");
    for (key in expectedGroupMap) {
        var groupObj = actualGroupMap[key];
        assertNotNull(groupObj, "/groupMap[" + key + "]");
        assertEquals(groupObj.name, key, "/groupMap[" + key + "]/name");
        var actualItems = groupObj.items.value;
        assertNotNull(actualItems, "/groupMap[" + key + "]/items/value")
    }
    
    var expectedVarMap = model.temperatureMap;
    var actualVarMap = netcdf.temperatureMap;
    assertNotNull(actualVarMap, "/temperatureMap");
    for (key in expectedVarMap) {
        var actualVar = actualVarMap[key];
        assertNotNull(actualVar, "/temperatureMap[" + key + "]")
        var expectedVar = expectedVarMap[key]
        assertEquals(actualVar.longName, expectedVar.longName, "/temperatureMap[" + key + "].longName");
        assertTrue(arrayEquals(actualVar.value.storage, expectedVar.value.storage), "/temperatureMap[" + key + "].value")
    }
}

function editModel(netcdf) {
    var group = new Packages.hsousa.ncml.io.test.GroupMapVO();
    group.name = "g02";
    var IntArray = Java.type("int[]");
    var shape = new IntArray(1);
    shape[0] = 10;
    var ncArray = Java.type("ucar.ma2.Array").factory(Java.type("ucar.ma2.DataType").INT, shape);
    for (var it = ncArray.getIndexIterator(); it.hasNext(); ) {
        it.setIntNext(Math.round(random(0, 100)));
    }
    group.items = new Packages.hsousa.ncml.io.test.GroupMapVO.ItemsVO();
    group.items.value = ncArray;
    netcdf.groupMap["g02"] = group;

    var avgTemp = new Packages.hsousa.ncml.io.test.TestNetcdfVO.TemperatureMapVO();
    avgTemp.longName = "average temperature";
    avgTemp.setValue(createNcArray(Java.type("ucar.ma2.DataType").FLOAT, [], function(it) {
        it.setDoubleNext(random(10, 20));
    }).getObject(scalarIndex));
    netcdf.temperatureMap["temp_average"] = avgTemp;

    return netcdf;
}

function verifyEditedFile(netcdf, model) {
    verifyCreatedFile(netcdf, model);
}