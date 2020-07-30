package hsousa.netcdf.schemagen;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class SchemaWrapper extends AbstractGroupWrapper {

    private Netcdf schema;
    private String packageName;
    private String groupName;

    public SchemaWrapper(Netcdf schema, String packageName, String groupName, Properties properties) {
        super(null, properties);
        this.schema = schema;
        this.packageName = packageName;
        this.groupName = substitute("/", groupName);
    }

    @Override
    public String getName() {
        return groupName;
    }
    
    @Override
    public String getFullName() {
        return "";
    }

    @Override
    public String getNameTag() {
        throw new IllegalStateException("This should not have been called for the root group!");
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public List<AbstractGroupWrapper> getGroups() {
        return schema.getEnumTypedefOrGroupOrDimension().stream().filter(element -> element instanceof Group)
                .map(element -> new GroupWrapper(this, (Group) element, packageName, properties))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dimension> getDimensions() {
        return schema.getEnumTypedefOrGroupOrDimension().stream().filter(element -> element instanceof Dimension)
                .map(element -> (Dimension) element).collect(Collectors.toList());
    }

    @Override
    protected List<Attribute> getAttributesImpl() {
        return schema.getEnumTypedefOrGroupOrDimension().stream().filter(element -> element instanceof Attribute)
                .map(element -> (Attribute) element).collect(Collectors.toList());
    }

    @Override
    public List<VariableWrapper> getVariables() {
        return schema.getEnumTypedefOrGroupOrDimension().stream().filter(element -> element instanceof Variable)
                .map(variable -> new VariableWrapper(this, properties, (Variable) variable))
                .collect(Collectors.toList());
    }

}
