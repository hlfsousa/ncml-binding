package io.github.hlfsousa.ncml.io.wrapper;

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
