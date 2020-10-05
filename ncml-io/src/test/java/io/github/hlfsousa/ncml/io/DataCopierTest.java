package io.github.hlfsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.io.cami.CommunityAtmosphericModel;
import io.github.hlfsousa.ncml.io.cami.CommunityAtmosphericModelVO;
import io.github.hlfsousa.ncml.io.read.NetcdfReader;
import io.github.hlfsousa.ncml.io.write.NetcdfWriter;

public class DataCopierTest {

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
