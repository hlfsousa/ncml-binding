package io.github.hlfsousa.ncml.example.readwrite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;
import io.github.hlfsousa.ncml.schemagen.improvements.HeaderTransformer;
import io.github.hlfsousa.ncml.schemagen.improvements.filtering.ElementFilter;
import io.github.hlfsousa.ncml.schemagen.improvements.filtering.GroupArchetypeFilter;
import io.github.hlfsousa.ncml.schemagen.improvements.filtering.VariableAttributeFilter;

/**
 * This class executes the transformation configured for the CAMI header. All actual transformations are at
 * {@link CamiHeaderTransformer}. The transformed header is then writen to {@code src/main/resources}.
 * 
 * @author Henrique Sousa
 */
public class HeaderTransformation {

    private static final String NCML_SCHEMA_LOCATION = "http://www.unidata.ucar.edu/namespaces/netcdf/ncml-2.2 "
            + "https://www.unidata.ucar.edu/schemas/netcdf/ncml-2.2.xsd";
    private static final String CAMI_PATH = "../samples/cami_0000-09-01_64x128_L26_c030918.xml";
    private static final String DEST_PATH = "src/main/resources/cami_archetype.xml";
    private static final String PROPERTIES_PATH = "src/main/resources/cami_archetype.properties";

    private CamiHeaderTransformer transformer;
    private final JAXBContext jaxbContext;

    public static void main(String[] args) throws Exception {
        new HeaderTransformation().improveHeader();
    }

    public HeaderTransformation() {
        try {
            jaxbContext = JAXBContext.newInstance(Netcdf.class);
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
        transformer = new CamiHeaderTransformer();
    }

    private void improveHeader() throws JAXBException, IOException {
        URL schemaURL = new File(CAMI_PATH).toURI().toURL();
        Netcdf originalHeader = (Netcdf) jaxbContext.createUnmarshaller().unmarshal(schemaURL.openStream());
        Netcdf transformedHeader = transformer.modify(originalHeader);
        File destFile = new File(DEST_PATH);
        destFile.getParentFile().mkdirs();
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, NCML_SCHEMA_LOCATION);
        marshaller.marshal(transformedHeader, destFile);
        File propertiesFile = new File(PROPERTIES_PATH);
        transformer.getGenerationProperties().store(new FileWriter(propertiesFile), null);
    }

}

/**
 * Thi class starts as a default transformer. Filters were added according to each transformation.
 * 
 * @author Henrique Sousa
 */
class CamiHeaderTransformer extends HeaderTransformer {

    /**
     * This filter merges variables with the same attributes into a mapped variable. If the {@code units} attributes
     * matches and the {@code long_name} attributes have the same prefix or suffix, the variables are merged.
     */
    protected final ElementFilter<Variable> unitsFilter = new VariableAttributeFilter().withCommonValue("units")
            .withNameBasedOn("long_name");

    /**
     * This filter merges variables with the same attributes into a mapped variable. If the {@code units} attribute is
     * missing, the {@code _FillValue} attributes matches and the name attributes have the same prefix or suffix, the
     * variables are merged.
     */
    protected final ElementFilter<Variable> fillValueFilter = new VariableAttributeFilter()
            .withCommonValue("_FillValue").withNameBasedOn("long_name").withAbsentValue("units")
            .withSequentialMatching(true);

    /**
     * This filter adds a property to the generation properties, so that the property that corresponds to a variable is
     * created with a more intuitive name based on the {@code long_name} attribute.
     */
    protected final ElementFilter<Variable> nameChangingFilter = new ElementFilter<Variable>() {

        private final Pattern nameTooShort = Pattern.compile("\\w{1,10}");

        @Override
        public List<Variable> apply(List<Variable> variableList) {
            for (Variable variable : variableList) {
                String longName = variable.getAttribute().stream()
                        .filter(attribute -> attribute.getName().equals("long_name")).findAny()
                        .map(attribute -> attribute.getValue()).orElse(null);
                if (longName == null) {
                    continue;
                }
                if (nameTooShort.matcher(variable.getName()).matches()) {
                    generationProperties.setProperty("substitution./" + variable.getName(), makeName(longName));
                }
            }
            return variableList;
        }

        private String makeName(String longName) {
            return longName.trim().toLowerCase().replace(' ', '_').replace('-', '_').replaceAll("\\(.*?\\)", "")
                    .replaceAll("^_+", "").replaceAll("_++$", "");
        }

    };

    /**
     * This filter makes sure identical groups are merged into a mapped group. The header does not declare any group, so
     * this filter is not used.
     */
    protected final ElementFilter<Group> groupFilter = new GroupArchetypeFilter();

    protected Properties generationProperties = new Properties();

    public Properties getGenerationProperties() {
        return generationProperties;
    }

    @Override
    protected List<ElementFilter<Group>> getGroupFilters() {
        return Arrays.asList(groupFilter);
    }

    @Override
    protected List<ElementFilter<Variable>> getVariableFilters() {
        return Arrays.asList(unitsFilter, fillValueFilter, nameChangingFilter);
    }

    /**
     * The initial transformation results in some variables with names that are not ideal. This override does some post
     * processing for hard-coded improvements upon the initial product. Such mofifications are:
     * <ul>
     * <li>Variables {@code lev} and {@code ilev} ar merged to mapped variable {@code hybrid_level_at}. This is renamed
     * to {@code hybrid_level}.</li>
     * <li>Variables {@code U} (Zonal wind) and {@code V} are merged to mapped variable {@code onal_wind}. This is
     * renamed to {@code wind}.</li>
     * <li>Variables {@code CLDLIQ} (Grid box averaged liquid condensate amount) and {@code CLDICE} and (Grid box
     * averaged ice condensate amount) are merged to mapped variable {@code Grid_box_averaged}. This is renamed to
     * {@code grid_box_averaged_condensate_amount}.</li>
     * <li>Variables {@code LANDM} and {@code LANDM_COSLAT} are merged to mapped variable
     * {@code land ocean transition mask: ocean (0), continent (1), transition (0-1)}. This is renamed to
     * {@code land_ocean_transition_mask}.</li>
     * <li>Variables {@code TS}, {@code TSICE}, {@code TS1}, {@code TS2}, {@code TS3}, {@code TS4}, {@code TBOT} are
     * merged to mapped variable {@code temperature}. To avoid conflict, this is renamed to
     * {@code secondary_temperature}.</li>
     * <li>Some variables have names that are not representative of their semantics. For instance, {@code SICTHK} (sea
     * ice thickness). The generated code would be more intuitive and clear if we had a method
     * {@code getSeaIceThickness()} instead of {@code getSicthk()}. So if the final variable name is a single word (no
     * whitespace, no underscore) and there is a {@code long_name} attribute, an entry is created in a properties file
     * to create a property name for that variable that matches the attribute rather than the variable name.</li>
     * </ul>
     */
    @Override
    public Netcdf modify(Netcdf schema) {
        Netcdf archetype = super.modify(schema);

        Map<String, String> groupVariableSubstitution = new HashMap<>();
        groupVariableSubstitution.put("hybrid_level_at", "hybrid_level");
        groupVariableSubstitution.put("onal_wind", "wind");
        groupVariableSubstitution.put("Grid_box_averaged", "grid_box_averaged_condensate_amount");
        groupVariableSubstitution.put("land_ocean_transition_mask:_ocean_(0),_continent_(1),_transition_(0-1)",
                "land_ocean_transition_mask");
        groupVariableSubstitution.put("temperature", "secondary_temperature");

        for (Entry<String, String> entry : groupVariableSubstitution.entrySet()) {
            String prefix = entry.getKey() + ':';
            Optional<Variable> variableFound = archetype.getEnumTypedefOrGroupOrDimension().stream()
                    .filter(child -> child instanceof Variable).map(variable -> (Variable) variable)
                    .filter(variable -> variable.getName().startsWith(prefix)).findAny();
            if (variableFound.isPresent()) {
                Variable variable = variableFound.get();
                variable.setName(entry.getValue() + variable.getName().substring(entry.getKey().length()));
            } else {
                System.out.println(
                        archetype.getEnumTypedefOrGroupOrDimension().stream().filter(child -> child instanceof Variable)
                                .map(child -> ((Variable) child).getName()).collect(Collectors.joining(", ")));
                throw new IllegalStateException("There should be a mapped variable '" + entry.getKey() + "'");
            }
        }

        return archetype;
    }

}
