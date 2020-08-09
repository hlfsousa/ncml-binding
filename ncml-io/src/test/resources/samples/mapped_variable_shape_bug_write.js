importPackage(Packages.hsousa.ncml.io.test);
importPackage(Packages.ucar.ma2);

function createModel(model) {
	model = new Packages.hsousa.ncml.io.test.TestNetcdfVO();
	model.hybridLevel = new java.util.LinkedHashMap();
	
	var levLength = 26;
	var lev = new Packages.hsousa.ncml.io.test.TestNetcdfVO.HybridLevelVO();
	lev.longName = "hybrid level at midpoints (1000*(A+B))";
	lev.units = "level";
	lev.standardName = "atmosphere_hybrid_sigma_pressure_coordinate";
	lev.formulaTerms = "a: hyam b: hybm p0: P0 ps: PS";
	lev.fillValue = 9.99999961690316e+35;
	lev.dimensions = new java.util.ArrayList();
	lev.dimensions.add(new Packages.ucar.nc2.Dimension("lev", levLength, true, false, false));
	lev.value = createNcArray(Java.type("ucar.ma2.DataType").DOUBLE, [levLength], function(it) {
        it.setDoubleNext(random(0, 10000));
    });
	model.hybridLevel["lev"] = lev;
	
	var ilevLength = 27;
	var ilev = new Packages.hsousa.ncml.io.test.TestNetcdfVO.HybridLevelVO();
	ilev.longName = "hybrid level at interfaces (1000*(A+B))";
	ilev.units = "level";
	ilev.standardName = "atmosphere_hybrid_sigma_pressure_coordinate";
	ilev.formulaTerms = "a: hyai b: hybi p0: P0 ps: PS";
	ilev.fillValue = 9.99999961690316e+35;
	ilev.dimensions = new java.util.ArrayList();
	ilev.dimensions.add(new Packages.ucar.nc2.Dimension("ilev", ilevLength, true, false, false));
	ilev.value = createNcArray(Java.type("ucar.ma2.DataType").DOUBLE, [ilevLength], function(it) {
        it.setDoubleNext(random(0, 10000));
    });
	model.hybridLevel["ilev"] = ilev;
	
    return model;
}

function verifyCreatedFile(netcdf) {
	// the goal of this test is to verify the dimensions, not the contents of the variables
	assertEquals(netcdf.hybridLevel["lev"].dimensions[0].shortName, "lev", "/lev");
	assertEquals(netcdf.hybridLevel["ilev"].dimensions[0].shortName, "ilev", "/ilev");
}

function editModel(netcdf) {
	return netcdf;
}

function verifyEditedFile(netcdf, model) {
	// nothing to check
	verifyCreatedFile(netcdf, model)
}