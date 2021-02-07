package io.github.hlfsousa.ncml.io.wrapper;

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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.io.RuntimeConfiguration;
import io.github.hlfsousa.ncml.io.wrapper.NetcdfWrapper;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.nc2.Group;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;
import ucar.nc2.Variable;

public class NetcdfWrapperTest {

    private static class TestWrapper extends NetcdfWrapper {
        
        public TestWrapper(Group group, RuntimeConfiguration runtimeConfiguration) {
            super(group, runtimeConfiguration);
        }
        
        public Array getBoundaryLayerHeight() {
            // this has a scale factor and offset (see ECMWF_ERA-40_subset.cdl)
            return getNumericArray(group.findVariable("blh"));
        }
        
    }
    
    @Test
    public void testGetScaledVariable() throws Exception {
        String location = new File(getClass().getResource("/samples/ECMWF_ERA-40_subset.nc").toURI()).getAbsolutePath();
        try (NetcdfFile netcdf = NetcdfFiles.open(location)) {
            Group rootGroup = netcdf.getRootGroup();
            Variable blh = rootGroup.findVariable("blh");
            Array rawArray = blh.read();

            TestWrapper testSubject = new TestWrapper(rootGroup, new RuntimeConfiguration(Collections.emptyMap()));
            Array scaledArray = testSubject.getBoundaryLayerHeight();

            assertThat(rawArray.getShape(), is(scaledArray.getShape()));
            double scaleFactor = 0.108739383344517; // assuming attribute value
            double addOffset = 3570.14367055165; // assuming attribute value
            short missingValue = -32767;
            for (IndexIterator rawIterator = rawArray.getIndexIterator(), scaledIterator = scaledArray
                    .getIndexIterator(); rawIterator.hasNext(); ) {
                double actualValue = scaledIterator.getDoubleNext();
                if (rawIterator.getShortNext() == missingValue) {
                    assertThat(Arrays.toString(rawIterator.getCurrentCounter()), Double.isNaN(actualValue));
                } else {
                    double expectedValue = rawIterator.getDoubleCurrent() * scaleFactor + addOffset;
                    assertThat(Arrays.toString(rawIterator.getCurrentCounter()),
                            expectedValue, is(closeTo(actualValue, 1e-11)));
                }
            }
        }
    }

}
