package io.github.hlfsousa.ncml.schemagen;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import edu.ucar.unidata.netcdf.ncml.Attribute;

public class AttributeWrapper extends AbstractNode {

    private static final List<String> SPACE_SEPARATED_TYPES = Arrays.asList("boolean", "byte", "short", "int", "long", "float",
            "double");
    private static final String SPACE_SEPARATOR = " ";

    private Attribute attribute;
    private String name;

    public AttributeWrapper(AbstractAttributeContainer parent, Properties properties, Attribute attribute) {
        super(parent, properties);
        this.attribute = attribute;
        this.name = substitute("substitution", parent.getFullName() + "/@" + attribute.getName(), attribute.getName());
        // separator has to be set manually
        String value = attribute.getValue();
        if (value != null && !value.isEmpty()) {
            if (SPACE_SEPARATED_TYPES.contains(attribute.getType()) && value.contains(SPACE_SEPARATOR)
                    && (attribute.getSeparator() == null || attribute.getSeparator().isEmpty())) {
                attribute.setSeparator(SPACE_SEPARATOR);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getShapeBrackets() {
        if (attribute.getSeparator() == null) {
            return "";
        }
        // TBC can an attribute have more than one dimension?
        int rank = 1;
        StringBuilder str = new StringBuilder(rank * 2);
        for (int i = 0; i < rank; i++) {
            str.append("[]");
        }
        return str.toString();
    }

}
