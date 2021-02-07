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

var DoubleArray = Java.type("double[]");

function createModel(model) {
	model = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO();
	model.hybridLevel = new java.util.LinkedHashMap();
	
	var levLength = 26;
	var lev = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.HybridLevelVO();
	lev.longName = "hybrid level at midpoints (1000*(A+B))";
	lev.units = "level";
	lev.standardName = "atmosphere_hybrid_sigma_pressure_coordinate";
	lev.formulaTerms = "a: hyam b: hybm p0: P0 ps: PS";
	lev.fillValue = 9.99999961690316e+35;
	lev.dimensions = new java.util.ArrayList();
	lev.dimensions.add(new Packages.ucar.nc2.Dimension("lev", levLength, true, false, false));
	lev.value = function() {
	    var value = new DoubleArray(levLength);
        for (var i = 0; i < 10; i++) {
            value[i] = random(0, 10000);
        }
        return value;
    }();
	model.hybridLevel["lev"] = lev;
	
	var ilevLength = 27;
	var ilev = new Packages.io.github.hlfsousa.ncml.io.test.TestNetcdfVO.HybridLevelVO();
	ilev.longName = "hybrid level at interfaces (1000*(A+B))";
	ilev.units = "level";
	ilev.standardName = "atmosphere_hybrid_sigma_pressure_coordinate";
	ilev.formulaTerms = "a: hyai b: hybi p0: P0 ps: PS";
	ilev.fillValue = 9.99999961690316e+35;
	ilev.dimensions = new java.util.ArrayList();
	ilev.dimensions.add(new Packages.ucar.nc2.Dimension("ilev", ilevLength, true, false, false));
	ilev.value = function() {
	    var value = new DoubleArray(ilevLength);
        for (var i = 0; i < 10; i++) {
            value[i] = random(0, 10000);
        }
        return value;
    }();
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
