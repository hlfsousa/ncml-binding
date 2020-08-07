package hsousa.netcdf.schemagen;

import java.util.Properties;

import edu.ucar.unidata.netcdf.ncml.Attribute;

public class AttributeWrapper extends AbstractNode {

    private Attribute attribute;
    private String name;

    public AttributeWrapper(AbstractAttributeContainer parent, Properties properties, Attribute attribute) {
        super(parent, properties);
        this.attribute = attribute;
        this.name = substitute("substitution", parent.getFullName() + "/@" + attribute.getName(), attribute.getName());
    }

    public String getName() {
        return name;
    }

    public Attribute getAttribute() {
        return attribute;
    }

}
