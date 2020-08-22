package hsousa.netcdf.schemagen.improvements;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class HeaderTransformerTest {

    private Netcdf schema;
    private HeaderTransformer transformer;

    @BeforeEach
    public void setupTransformer() throws Exception {
        URL schemaURL = new File("../ncml-binding-examples/samples/cami_0000-09-01_64x128_L26_c030918.xml")
                .toURI().toURL();
        JAXBContext context = JAXBContext.newInstance(Netcdf.class);
        schema = (Netcdf) context.createUnmarshaller().unmarshal(schemaURL.openStream());
        transformer = new DefaultHeaderTransformer();
    }
    
    @Test
    public void testVariableMergingByUnitLongName() {
        Netcdf transformed = transformer.modify(schema);
        Map<String, Variable> variables = getChildren(transformed.getEnumTypedefOrGroupOrDimension(), Variable.class,
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
            Map<String, Attribute> attributes = getChildren(hybridLevel.getAttribute(), Attribute.class,
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
            Map<String, Attribute> attributes = getChildren(wind.getAttribute(), Attribute.class,
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

    private <T> Map<String, T> getChildren(final List<?> childList, Class<?> targetType, Function<T, String> keyMapper) {
        @SuppressWarnings("unchecked")
        Map<String, T> children = childList.stream()
                .filter(child -> targetType.isInstance(child))
                .map(child -> (T) child)
                .collect(Collectors.toMap(keyMapper, child -> child));
        return children;
    }
    
}
