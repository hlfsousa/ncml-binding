package io.github.hlfsousa.ncml.io;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.io.cami.CommunityAtmosphericModel;
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
    
}
