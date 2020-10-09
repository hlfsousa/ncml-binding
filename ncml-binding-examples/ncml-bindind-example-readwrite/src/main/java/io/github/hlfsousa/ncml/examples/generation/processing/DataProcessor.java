package io.github.hlfsousa.ncml.examples.generation.processing;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelVO;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelWrapper;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel.HybridLevelVariable;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel.LatitudeVariable;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel.LongitudeVariable;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel.ReferencePressureVariable;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModel.TimeVariable;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelInitializer;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelInitializer.HybridLevelInitializer;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelInitializer.LatitudeInitializer;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelInitializer.LongitudeInitializer;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelInitializer.ReferencePressureInitializer;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelInitializer.TimeInitializer;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelVO.HybridLevelVO;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelVO.LatitudeVO;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelVO.LongitudeVO;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelVO.ReferencePressureVO;
import io.github.hlfsousa.ncml.examples.generation.CommunityAtmosphericModelVO.TimeVO;
import io.github.hlfsousa.ncml.io.read.NetcdfReader;
import io.github.hlfsousa.ncml.io.write.NetcdfWriter;
import ucar.nc2.NetcdfFile;

/**
 * This is a class that creates, consumes and produces Community Atmospheric Model data. Of course, this is an example
 * and the data herein is not based on reality. It just illustrates the amount of effort needed to read and write the
 * data in the NetCDF streams and objects exchanged.
 * 
 * @author Henrique Sousa
 */
public class DataProcessor {

    private static final int LAT_SLICES = 64;
    private static final int LON_SLICES = 128;

    /**
     * Creates data for a new file, then saves it.
     * 
     * @param dest where to save the new file
     * @throws IOException if the file cannot be saved
     */
    public void create(File dest) throws IOException {
        CommunityAtmosphericModelVO cami = startFromScratch();
        cami.setLatitude(createLatitude());
        cami.setLongitude(createLongitude());

        cami.setHybridLevel(new HashMap<>());
        cami.getHybridLevel().put("lev", createHybridLevel(26, "lev"));
        cami.getHybridLevel().put("ilev", createHybridLevel(27, "ilev"));

        cami.setTime(createTime());

        cami.setReferencePressure(createReferencePressure());

        save(cami, dest);
    }
    
    /**
     * Reads a file, processes it, then saves to the destination.
     * 
     * @param src the source file
     * @param dest where to save the modified file
     * @throws IOException if any of the files cannot be read/saved
     */
    public void process(File src, File dest) throws IOException {
        NetcdfReader<CommunityAtmosphericModel> reader = new NetcdfReader<>(CommunityAtmosphericModel.class);
        CommunityAtmosphericModel cami = reader.read(src, false);
        String history = cami.getHistory();
        String entry = "DataProcessor.process()";
        cami.setHistory(history == null ? entry : (history + "\n" + entry));
        // other changes (stored in memory)
        save(cami, dest);
    }

    /**
     * Saves data to a predefined location.
     * 
     * @param cami Data to save
     * @param dest Where to save the data
     * @throws IOException if the file cannot be saved
     */
    protected void save(CommunityAtmosphericModel cami, File dest) throws IOException {
        NetcdfWriter writer = new NetcdfWriter(true);
        writer.setDefaultAttributeValueUsed(true);
        NetcdfFile netcdf = writer.write(cami, dest);
        netcdf.close(); // it is important to close the file after writing it
    }

    private ReferencePressureVariable<Double> createReferencePressure() {
        ReferencePressureVO refPressure = new ReferencePressureVO();
        refPressure.setValue(20d);
        ReferencePressureInitializer.initialize(refPressure);
        return refPressure;
    }

    private TimeVariable<double[]> createTime() {
        TimeVO time = new TimeVO();
        Instant initialValue = Instant.now().minus(Duration.ofDays(60));
        Instant referenceInstant = Instant.from(DateTimeFormatter.ISO_INSTANT.parse("0000-08-29T00:00:00Z"));
        double interval = 5d / 86400d; // 5 seconds
        double[] value = new double[1000];
        value[0] = Duration.between(referenceInstant, initialValue).getSeconds() / 86400d;
        for (int i = 1; i < 1000; i++) {
            value[i] = value[i - 1] + interval;
        }
        time.setValue(value);
        TimeInitializer.initialize(time);
        return time;
    }

    /**
     * Creates variable lev or ilev.
     * 
     * @param length   dimension of particular dimension.
     * @param longName value of attribute long_name
     * @return
     */
    private HybridLevelVariable<double[]> createHybridLevel(int length, String name) {
        Random rng = new Random();
        HybridLevelVO hybridLevel = new HybridLevelVO();
        // we only care about value, the rest is in the initializer
        // this section is where actual code would deal with what matters: the value of the variable
        double[] value = new double[length];
        double range = 100;
        for (int i = 0; i < length; i++) {
            value[i] = Math.abs(rng.nextDouble()) * range;
        }
        // long_name, units, positive, standard_name, formula_terms, _FillValue and shape
        HybridLevelInitializer.initialize(name, hybridLevel);
        return hybridLevel;
    }

    /**
     * As is the case with the default global attributes, each variable must be initialized individually -- including
     * attribute conventions.
     * 
     * @param globalDimensions
     * 
     * @return initialized latitude variable
     */
    private LatitudeVariable<double[]> createLatitude() {
        // mandatory code: set variable value
        LatitudeVO latitude = new LatitudeVO();
        double[] latitudeValue = new double[LAT_SLICES];
        for (int slice = 0; slice < LAT_SLICES; slice++) {
            latitudeValue[slice] = -180 + (360d / LAT_SLICES) * slice;
        }
        latitude.setValue(latitudeValue);
        // set attributes
        LatitudeInitializer.initialize(latitude);
        return latitude;
    }

    private LongitudeVariable<double[]> createLongitude() {
        LongitudeVO longitude = new LongitudeVO();
        double[] longitudeValue = new double[LON_SLICES];
        for (int slice = 0; slice < LON_SLICES; slice++) {
            longitudeValue[slice] = (360d / LON_SLICES) * slice;
        }
        longitude.setValue(longitudeValue);
        LongitudeInitializer.initialize(longitude);
        return longitude;
    }

    /**
     * This method uses the initializer feature to set default information.
     * 
     * @return a newly initialized modifiable model
     */
    private CommunityAtmosphericModelVO startFromScratch() {
        CommunityAtmosphericModelVO cami = new CommunityAtmosphericModelVO();
        cami.setSource("Hard-coded example");
        cami.setCase("cam2run");
        cami.setTitle("NcML mapping to Java model example");
        cami.setHistory("DataProcessor.startFromScratch()");
        // nothing that was set is overwritten
        CommunityAtmosphericModelInitializer.initialize(cami);
        return cami;
    }

}
