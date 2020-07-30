package hsousa.netcdf.schemagen;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class GroupWrapper extends AbstractGroupWrapper {

    private Group group;
    private String groupName;
    private String packageName;
    private boolean mapped = false;

    public GroupWrapper(AbstractGroupWrapper parent, Group group, String packageName, Properties properties) {
        super(parent, properties);
        this.group = group;
        if (group.getName().matches("[a-zA-Z_0-9]+:.*")) {
            // mapped group
            String baseName = group.getName().substring(0, group.getName().indexOf(':'));
            this.groupName = substitute(parent.getFullName() + '/' + baseName, baseName);
            mapped = true;
        } else {
            this.groupName = substitute(parent.getFullName() + '/' + group.getName(), group.getName());
        }
        this.packageName = packageName;
    }

    @Override
    public boolean isMapped() {
        return mapped;
    }

    public String getMapExpression() {
        return group.getName().substring(group.getName().indexOf(':') + 1);
    }

    @Override
    public String getName() {
        return groupName == null ? group.getName() : groupName;
    }

    @Override
    public String getNameTag() {
        return group.getName();
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public List<AbstractGroupWrapper> getGroups() {
        return group.getEnumTypedefOrDimensionOrVariable().stream().filter(element -> element instanceof Group)
                .map(element -> new GroupWrapper(this, (Group) element, packageName, properties))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dimension> getDimensions() {
        return group.getEnumTypedefOrDimensionOrVariable().stream().filter(element -> element instanceof Dimension)
                .map(element -> (Dimension) element).collect(Collectors.toList());
    }

    @Override
    protected List<Attribute> getAttributesImpl() {
        return group.getEnumTypedefOrDimensionOrVariable().stream().filter(element -> element instanceof Attribute)
                .map(element -> (Attribute) element).collect(Collectors.toList());
    }

    @Override
    public List<VariableWrapper> getVariables() {
        return group.getEnumTypedefOrDimensionOrVariable().stream().filter(element -> element instanceof Variable)
                .map(variable -> new VariableWrapper(this, properties, (Variable) variable))
                .collect(Collectors.toList());
    }

}
