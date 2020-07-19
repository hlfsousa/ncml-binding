importPackage(Packages.hsousa.ncml.io.test);
importPackage(Packages.ucar.ma2);

function random(min, max) {
	var delta = max - min;
	var base = Math.random() * delta;
	return base + min;
}

function createModel(model) {
	model = new Packages.hsousa.ncml.io.test.TestNetcdfVO();
	var group = new Packages.hsousa.ncml.io.test.GroupMapVO();
	group.name = "g01";
	var IntArray = Java.type("int[]");
	var shape = new IntArray(10);
	var ncArray = Java.type("ucar.ma2.Array").factory(Java.type("ucar.ma2.DataType").INT, shape);
    for (var it = ncArray.getIndexIterator(); it.hasNext(); ) {
		it.setIntNext(Math.round(random(0, 100)));
    }
    group.items = ncArray;
    model.groupMap = { g01: group };
	return model;
}

function verifyCreatedFile(netcdf, model) {
	var expectedMap = model.groupMap;
	var actualMap = netcdf.groupMap;
	assertNotNull(actualMap, "/groupMap");
	for (key in expectedMap) {
		var groupObj = actualMap[key];
		assertNotNull(groupObj, "/groupMap[" + key + "]");
		assertEquals(groupObj.name, key, "/groupMap[" + key + "]/name");
	}
}

function editModel(netcdf) {
	var group = new Packages.hsousa.ncml.io.test.GroupMapVO();
	group.name = "g02";
	var IntArray = Java.type("int[]");
	var shape = new IntArray(10);
	var ncArray = Java.type("ucar.ma2.Array").factory(Java.type("ucar.ma2.DataType").INT, shape);
    for (var it = ncArray.getIndexIterator(); it.hasNext(); ) {
		it.setIntNext(Math.round(random(0, 100)));
    }
    group.items = ncArray;
    netcdf.groupMap["g02"] = group;
	return netcdf;
}

function verifyEditedFile(netcdf, model) {
	verifyCreatedFile(netcdf, model);
}