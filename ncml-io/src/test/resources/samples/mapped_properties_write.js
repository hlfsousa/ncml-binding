importPackage(Packages.io.github.hlfsousa.ncml.io.test);
importPackage(Packages.ucar.ma2);

var IntArray = Java.type("int[]");

function createModel(model) {
    model = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO();
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

    return model;
}

function verifyCreatedFile(netcdf, model) {
    var expectedGroupMap = model.groupMap;
    var actualGroupMap = netcdf.groupMap;
    assertNotNull(actualGroupMap, "/groupMap");
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
        assertTrue(arrayEquals(actualVar.value, expectedVar.value), "/temperatureMap[" + key + "].value")
    }

    var expectedIntMap = model.intMap;
    var actualIntMap = netcdf.intMap;
    for (var key in expectedIntMap) {
	    assertNotNull(actualIntMap[key]);
        assertEquals(actualIntMap[key], expectedIntMap[key], key);
	}

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