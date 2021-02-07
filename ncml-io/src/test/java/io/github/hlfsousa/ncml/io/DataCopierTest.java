package io.github.hlfsousa.ncml.io;

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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.io.cami.CommunityAtmosphericModel;
import io.github.hlfsousa.ncml.io.cami.CommunityAtmosphericModelVO;
import io.github.hlfsousa.ncml.io.read.NetcdfReader;
import io.github.hlfsousa.ncml.io.write.NetcdfWriter;
import io.github.hlfsousa.ncml.schemagen.NCMLCodeGenerator;

public class DataCopierTest {

    @Disabled("enable manually when re-generation is required (classes are versioned)")
    @Test
    public void generateModel() throws Exception {
        File sourcesDir = new File("src/test/java");
        // /cami_archetype.xml
        final String schemaDir = "../ncml-binding-examples/ncml-binding-example-readwrite/src/main/resources/";
        final String schemaLocation = schemaDir + "cami_archetype.xml";
        final String propertiesLocation = schemaDir + "cami_archetype.properties";
        final String rootPackage = "io.github.hlfsousa.ncml.io.cami";
        final String rootGroupName = "CommunityAtmosphericModel";
        
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(propertiesLocation)) {
            properties.load(reader);
        }
        properties.setProperty(NCMLCodeGenerator.CFG_PROPERTIES_LOCATION, "target/ncml-binding.properties");
        NCMLCodeGenerator generator = new NCMLCodeGenerator(new File(schemaLocation).toURI().toURL(),
                properties);
        generator.setModelPackage(rootPackage);
        generator.setRootGroupName(rootGroupName);
        generator.generateSources(sourcesDir);
    }
    
    @Test
    public void testCopyCami() throws Exception {
        NetcdfReader<CommunityAtmosphericModel> camiReader = new NetcdfReader<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel model = camiReader.read(new File("../ncml-binding-examples/samples/cami_0000-09-01_64x128_L26_c030918.nc"), true);
        DataCopier<CommunityAtmosphericModel> copier = new DataCopier<CommunityAtmosphericModel>() {};
        CommunityAtmosphericModel camiCopy = copier.copy(model);
        assertThat(camiCopy.getCase(), is(model.getCase()));
        assertThat(camiCopy.getCurrentDate(), is(notNullValue()));
        assertThat(camiCopy.getCurrentDate().getValue(), is(model.getCurrentDate().getValue()));
        assertThat(camiCopy.getSecondaryTemperature(), is(notNullValue()));
        assertThat(camiCopy.getSecondaryTemperature().keySet(), is(model.getSecondaryTemperature().keySet()));
        // this model does not declare: groups, List<anything but Dimension> (not officially supported)
    }
    
    @Test
    public void testWriteCopy() throws Exception {
        NetcdfReader<CommunityAtmosphericModel> camiReader = new NetcdfReader<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel model = camiReader.read(new File("../ncml-binding-examples/samples/cami_0000-09-01_64x128_L26_c030918.nc"), true);
        DataCopier<CommunityAtmosphericModel> copier = new DataCopier<CommunityAtmosphericModel>() {};
        CommunityAtmosphericModel camiCopy = copier.copy(model);

        // can copy be written?
        NetcdfWriter writer = new NetcdfWriter(true);
        File tempFile = File.createTempFile("testCopyCami", ".nc");
        tempFile.deleteOnExit();
        writer.write(camiCopy, tempFile);
    }
    
    @Test
    public void testInitializeFromHeader() throws Exception {
        File tmpFile = File.createTempFile("cami_header", ".nc");
        File logFile = File.createTempFile("output", ".log");
        tmpFile.deleteOnExit();
        logFile.delete();
        ProcessBuilder ncgenBuilder = new ProcessBuilder("ncgen", "-o", tmpFile.getPath(),
                "../ncml-binding-examples/samples/cami_0000-09-01_64x128_L26_c030918.cdl");
        ncgenBuilder.inheritIO();
        Process ncgen = ncgenBuilder.start();
        assertThat(ncgen.waitFor(), is(0));
        
        NetcdfReader<CommunityAtmosphericModel> reader = new NetcdfReader<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel header = reader.read(tmpFile, true);
        DataCopier<CommunityAtmosphericModel> copier = new DataCopier<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel valueObject = copier.copy(header);
        assertThat(valueObject.getLongitude(), is(notNullValue()));
        assertThat(valueObject.getLongitude().getDimensions(), is(notNullValue()));
    }
    
    @Test
    public void testCopyFromHeader() throws Exception {
        File tmpFile = File.createTempFile("cami_header", ".nc");
        File logFile = File.createTempFile("output", ".log");
        tmpFile.deleteOnExit();
        logFile.delete();
        ProcessBuilder ncgenBuilder = new ProcessBuilder("ncgen", "-o", tmpFile.getPath(),
                "../ncml-binding-examples/samples/cami_0000-09-01_64x128_L26_c030918.cdl");
        ncgenBuilder.inheritIO();
        Process ncgen = ncgenBuilder.start();
        assertThat(ncgen.waitFor(), is(0));
        
        NetcdfReader<CommunityAtmosphericModel> reader = new NetcdfReader<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel header = reader.read(tmpFile, true);
        DataCopier<CommunityAtmosphericModel> copier = new DataCopier<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel valueObject = new CommunityAtmosphericModelVO();
        copier.copy(header, valueObject);
        assertThat(valueObject.getLongitude(), is(notNullValue()));
        assertThat(valueObject.getLongitude().getDimensions(), is(notNullValue()));
    }
    
}
