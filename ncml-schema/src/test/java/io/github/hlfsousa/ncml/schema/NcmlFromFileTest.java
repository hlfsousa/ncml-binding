package io.github.hlfsousa.ncml.schema;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static io.github.hlfsousa.ncml.schema.NcmlSchemaUtil.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.jupiter.api.Test;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;

/*-
 * #%L
 * ncml-schema
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

public class NcmlFromFileTest {

    private static final String NCML_SCHEMA_LOCATION = "http://www.unidata.ucar.edu/namespaces/netcdf/ncml-2.2 "
            + "https://www.unidata.ucar.edu/schemas/netcdf/ncml-2.2.xsd";

    @Test
    public void testExtractHeader() throws Exception {
        try (NetcdfFile testFile = NetcdfFiles.open("../ncml-io/src/test/resources/samples/ECMWF_ERA-40_subset.nc")) {
            NcmlFromFile extractor = new NcmlFromFile();
            Netcdf header = extractor.extractHeader(testFile);
            
            Netcdf expectedHeader = readHeader("../ncml-io/src/test/resources/samples/ECMWF_ERA-40_subset.xml");
            verifyAttributes("", mapChildren(header.getEnumTypedefOrGroupOrDimension(), Attribute.class, att -> att.getName()),
                    mapChildren(expectedHeader.getEnumTypedefOrGroupOrDimension(), Attribute.class, att -> att.getName()));
            verifyVariables("", mapChildren(header.getEnumTypedefOrGroupOrDimension(), Variable.class, var -> var.getName()),
                    mapChildren(expectedHeader.getEnumTypedefOrGroupOrDimension(), Variable.class, var -> var.getName()));
            verifyDimensions("", mapChildren(header.getEnumTypedefOrGroupOrDimension(), Dimension.class, dim -> dim.getName()),
                    mapChildren(expectedHeader.getEnumTypedefOrGroupOrDimension(), Dimension.class, dim -> dim.getName()));
            verifyGroups("", mapChildren(header.getEnumTypedefOrGroupOrDimension(), Group.class, group -> group.getName()),
                    mapChildren(expectedHeader.getEnumTypedefOrGroupOrDimension(), Group.class, group -> group.getName()));
        }
    }

    private void verifyGroups(String string, Map<String, Group> mapChildren, Map<String, Group> mapChildren2) {
        // TODO implement
    }

    private void verifyDimensions(String path, Map<String, Dimension> actualDimensions,
            Map<String, Dimension> expectedDimensions) {
        assertThat(path + ": different dimensions", actualDimensions.keySet(), is(equalTo(expectedDimensions.keySet())));
        for (String key : actualDimensions.keySet()) {
            verify(path + "/" + key, actualDimensions.get(key), expectedDimensions.get(key));
        }
    }

    private void verify(String path, Dimension actual, Dimension expected) {
        assertThat(path, actual.getName(), is(expected.getName()));
        assertThat(path, actual.getLength(), is(expected.getLength()));
        assertThat(path, actual.isIsUnlimited(), is(expected.isIsUnlimited()));
        assertThat(path, actual.isIsVariableLength(), is(expected.isIsVariableLength()));
    }

    private void verifyVariables(String path, Map<String, Variable> actualVariables, Map<String, Variable> expectedVariables) {
        assertThat(path + ": different variables", actualVariables.keySet(), is(equalTo(expectedVariables.keySet())));
        for (String key : actualVariables.keySet()) {
            verify(path + "/" + key, actualVariables.get(key), expectedVariables.get(key));
        }
    }

    private void verify(String path, Variable actualVariable, Variable expectedVariable) {
        assertThat(path, actualVariable.getName(), is(expectedVariable.getName()));
        assertThat(path, actualVariable.getShape(), is(expectedVariable.getShape()));
        assertThat(path, actualVariable.getType(), is(expectedVariable.getType()));
        verifyAttributes(path, mapChildren(actualVariable.getAttribute(), Attribute.class, att -> att.getName()),
                mapChildren(expectedVariable.getAttribute(), Attribute.class, att -> att.getName()));
    }

    private void verifyAttributes(String path, Map<String, Attribute> actualAttributes, Map<String, Attribute> expectedAttributes) {
        assertThat(path + ": different attributes", actualAttributes.keySet(), is(equalTo(expectedAttributes.keySet())));
        for (String name : actualAttributes.keySet()) {
            verify(path + '/' + name, actualAttributes.get(name), expectedAttributes.get(name));
        }
    }

    private void verify(String path, Attribute actual, Attribute expected) {
        assertThat(path, actual.getName(), is(expected.getName()));
        assertThat(path, actual.getType(), is(expected.getType()));
        // either(...) does not work for some reason, but a simple is() is good for now
        //assertThat(path, actual.isIsUnsigned(),
        //        is((expected.isIsUnsigned() != null && expected.isIsUnsigned()) ? is(true)
        //                : either(is(false)).or(is(nullValue()))));
        assertThat(path, actual.isIsUnsigned(), is(expected.isIsUnsigned()));
        assertThat(path, actual.getSeparator(), is(expected.getSeparator()));
        if (Arrays.asList("float", "double").contains(actual.getType())) {
            // precision in the file is not necessarily the same
            double actualValue = Double.parseDouble(actual.getValue());
            double expectedValue = Double.parseDouble(expected.getValue());
            assertThat(path, actualValue, closeTo(expectedValue, 1E-6));
        } else {
            assertThat(path, actual.getValue(), is(expected.getValue()));
        }
    }

    private Netcdf readHeader(String location) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Netcdf.class);
        return (Netcdf) context.createUnmarshaller().unmarshal(new File(location));
    }
    
}
