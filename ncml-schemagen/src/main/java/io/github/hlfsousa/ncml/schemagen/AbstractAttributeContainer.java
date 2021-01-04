package io.github.hlfsousa.ncml.schemagen;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;

public abstract class AbstractAttributeContainer extends AbstractNode {

    protected String typeName;
    
    public AbstractAttributeContainer(AbstractAttributeContainer parent, Properties properties) {
        super(parent, properties);
    }
    
    public String getTypeName() {
        if (typeName == null) {
            typeName = camelCase(substitute(
                    "type", (parent != null ? parent.getFullName() : "") + '/' + getName(), getName()));
        }
        return typeName;
    }

    public abstract String getPackageName();

    public abstract String getNameTag();

    public abstract List<Dimension> getDimensions();

    public List<AttributeWrapper> getAttributes() {
        return getAttributesImpl().stream().map(attribute -> new AttributeWrapper(this, properties, attribute))
                .collect(Collectors.toList());
    }

    protected abstract List<Attribute> getAttributesImpl();

    @Override
    public void initializeConfiguration(Properties initialConfiguration) {
        super.initializeConfiguration(initialConfiguration);
        getAttributes().forEach(attribute -> attribute.initializeConfiguration(initialConfiguration));
    }

}