package io.github.hlfsousa.ncml.example.readwrite;

/*-
 * #%L
 * ncml-binding-example-readwrite
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
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel;
import io.github.hlfsousa.ncml.examples.generation.processing.DataProcessor;
import io.github.hlfsousa.ncml.io.read.NetcdfReader;

/**
 * This test shows how creating a model also facilitates testing.
 * 
 * @author Henrique Sousa
 */
public class DataProcessorTest {

    /**
     * One way to test the creation of a file is to test if the file is written without error.
     */
    @Test
    public void testCreateAndSave() throws IOException {
        DataProcessor processor = new DataProcessor();
        File tmpFile = File.createTempFile("testCreateAndSave", ".nc");
        tmpFile.deleteOnExit();
        processor.create(tmpFile);
        assertThat(tmpFile.length(), is(not(0L)));
        // with a file being written, we can check its content
        NetcdfReader<CommunityAtmosphericModel> reader = new NetcdfReader<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel cami = reader.read(tmpFile, true);
        assertThat(cami.getTitle(), is("NcML mapping to Java model example"));
        // all file content can be tested just by retrieving properties
    }

    /**
     * If saving is done in a particular call, you can do a black/gray box test for the content.
     */
    @Test
    public void testCreateContent() throws Exception {
        final CompletableFuture<CommunityAtmosphericModel> camiResult = new CompletableFuture<>();

        // this will intercept the saving of the file to test the content being saved
        DataProcessor processor = new DataProcessor() {
            @Override
            protected void save(CommunityAtmosphericModel cami, File dest) throws IOException {
                camiResult.complete(cami);
            }
        };
        processor.create(null);

        // once we have the data, we can check it
        assertThat(camiResult.isDone() && !camiResult.isCompletedExceptionally(), is(true));
        assertThat(camiResult.get(), is(notNullValue()));
        CommunityAtmosphericModel cami = camiResult.get();
        assertThat(cami.getTitle(), is("NcML mapping to Java model example"));
        // and thus we have independent testing of file contents before and after writing it
    }
    
    @Test
    public void testProcess() throws Exception {
        DataProcessor processor = new DataProcessor();
        File srcFile = File.createTempFile("testProcessA", ".nc");
        processor.create(srcFile);
        
        File dstFile = File.createTempFile("testProcessB", ".nc");
        processor.process(srcFile, dstFile);

        // with a file being written, we can check its content
        NetcdfReader<CommunityAtmosphericModel> reader = new NetcdfReader<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel cami = reader.read(dstFile, true);
        assertThat(cami.getTitle(), is("NcML mapping to Java model example"));
        assertThat(cami.getHistory(), is("DataProcessor.startFromScratch()\nDataProcessor.process()"));
        // test the rest of the content as you wish
    }

}
