package hsousa.netcdf.schemagen.improvements;

import static hsousa.ncml.schema.NcmlSchemaUtil.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;

import org.junit.jupiter.api.Test;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class HeaderTransformerTest {

    private Netcdf schema;
    private HeaderTransformer transformer;

    public void setupTransformer(String headerLocation) throws Exception {
        URL schemaURL = new File(headerLocation).toURI().toURL();
        JAXBContext context = JAXBContext.newInstance(Netcdf.class);
        schema = (Netcdf) context.createUnmarshaller().unmarshal(schemaURL.openStream());
        transformer = new DefaultHeaderTransformer();
    }
    
    @Test
    public void testGroupMergingByCommonDefinition() throws Exception {
        setupTransformer("../ncml-binding-examples/samples/test_hgroups.xml");
        Netcdf transformed = transformer.modify(schema);
        Map<String, Group> groups = mapChildren(transformed.getEnumTypedefOrGroupOrDimension(), Group.class,
                group -> group.getName().contains(":") ? group.getName().substring(0, group.getName().indexOf(':'))
                        : group.getName());

        {
            Group mozaicFlight = groups.get("mozaic_flight_2012030");
            assertThat(mozaicFlight, is(notNullValue()));
            String regex = mozaicFlight.getName().substring(mozaicFlight.getName().indexOf(':') + 1);
            assertThat("mozaic_flight_2012030412545335_ascent", matchesPattern(regex));
            Map<String, Dimension> dimensions = mapChildren(mozaicFlight.getEnumTypedefOrDimensionOrVariable(), Dimension.class,
                    dim -> dim.getName());
            /*
             * dimension size is not going to be accurate (each group has its own), but the actual value will be set to
             * and retrieved from the variable interface when reading and writing the file, respectively
             */
            assertThat(dimensions.get("air_press"), is(notNullValue()));
            
            Map<String, Variable> variables = mapChildren(mozaicFlight.getEnumTypedefOrDimensionOrVariable(), Variable.class,
                    var -> var.getName());
            assertThat(variables.keySet(),
                    is(new HashSet<>(Arrays.asList("air_press", "CO", "O3", "altitude", "UTC_time", "lat", "lon"))));

            Map<String, Attribute> attributes = mapChildren(mozaicFlight.getEnumTypedefOrDimensionOrVariable(), Attribute.class,
                    att -> att.getName());
            assertThat(attributes.keySet(), is(new HashSet<>(Arrays.asList("airport_dep", "flight", "level",
                    "airport_arr", "mission", "time_dep", "aircraft", "link", "phase", "time_arr"))));
        }
    }
    
    @Test
    public void testVariableMergingByUnitLongName() throws Exception {
        setupTransformer("../ncml-binding-examples/samples/cami_0000-09-01_64x128_L26_c030918.xml");
        Netcdf transformed = transformer.modify(schema);
        Map<String, Variable> variables = mapChildren(transformed.getEnumTypedefOrGroupOrDimension(), Variable.class,
                var -> var.getName());
        
        {
            Variable hybridLevel = variables.get("hybrid_level_at:lev|ilev");
            assertThat(hybridLevel, is(notNullValue()));
            assertThat(hybridLevel.getType(), is("double"));
            assertThat(hybridLevel.getShape().split("\\s++").length, is(1));
            String[] emptyAttributes = { "long_name", "formula_terms" };
            Map<String, String> notEmptyAttributes = new LinkedHashMap<>();
            notEmptyAttributes.put("units", "level");
            notEmptyAttributes.put("positive", "down");
            notEmptyAttributes.put("standard_name", "atmosphere_hybrid_sigma_pressure_coordinate");
            notEmptyAttributes.put("_FillValue", "9.99999961690316e+35");
            Map<String, Attribute> attributes = mapChildren(hybridLevel.getAttribute(), Attribute.class,
                    attribute -> attribute.getName());
            testAttributes(attributes, emptyAttributes, notEmptyAttributes);
        }

        {
            Variable wind = variables.get("onal_wind:U|V");
            assertThat(wind, is(notNullValue()));
            assertThat(wind.getShape().split("\\s++").length, is(4));
            assertThat(wind.getType(), is("double"));
            String[] emptyAttributes = { "long_name" };
            Map<String, String> notEmptyAttributes = new LinkedHashMap<>();
            notEmptyAttributes.put("units", "m/s");
            Map<String, Attribute> attributes = mapChildren(wind.getAttribute(), Attribute.class,
                    attribute -> attribute.getName());
            testAttributes(attributes, emptyAttributes, notEmptyAttributes);
        }
    }

    private void testAttributes(Map<String, Attribute> attributes, String[] emptyAttributes,
            Map<String, String> notEmptyAttributes) {
        for (String attributeName : emptyAttributes) {
            Attribute attribute = attributes.get(attributeName);
            assertThat(attribute.getValue(), is(emptyOrNullString()));
            assertThat(attribute.getType(), is(equalToIgnoringCase("string")));
        }
        
        for (Entry<String, String> notEmptyEntry : notEmptyAttributes.entrySet()) {
            Attribute attribute = attributes.get(notEmptyEntry.getKey());
            assertThat(attribute.getValue(), is(notEmptyEntry.getValue()));
        }
    }

}
