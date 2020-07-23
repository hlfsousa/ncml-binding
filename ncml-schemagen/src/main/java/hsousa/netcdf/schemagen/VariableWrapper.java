package hsousa.netcdf.schemagen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Variable;

public class VariableWrapper extends AbstractAttributeContainer {

    private final Variable variable;
    private final String name;
    private boolean mapped = false;

    public VariableWrapper(AbstractAttributeContainer parent, Properties properties, Variable variable) {
        super(parent, properties);
        this.variable = variable;
        if (variable.getName().matches("[a-zA-Z_0-9]+:.*")) {
            // mapped group
            String baseName = variable.getName().substring(0, variable.getName().indexOf(':'));
            this.name = substitute(parent.getFullName() + '.' + baseName, baseName);
            mapped = true;
        } else {
            this.name = substitute(parent.getFullName() + '.' + variable.getName(), variable.getName());
        }
    }

    public String getMapExpression() {
        return variable.getName().substring(variable.getName().indexOf(':') + 1);
    }

    public boolean isMapped() {
        return mapped;
    }

    public Variable getVariable() {
        return variable;
    }

    @Override
    public String getPackageName() {
        return parent.getPackageName();
    }

    @Override
    public String getNameTag() {
        return variable.getName();
    }

    @Override
    public List<Dimension> getDimensions() {
        return findDimensions(variable.getShape());
    }

    private List<Dimension> findDimensions(String shape) {
        if (shape == null || shape.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String[] dimNames = shape.trim().split("\\s++");
        List<Dimension> dimensionList = new ArrayList<>(dimNames.length);
        for (String dimensionName : dimNames) {
            dimensionList.add(findDimension(dimensionName));
        }
        return dimensionList;
    }

    private Dimension findDimension(String name) {
        Dimension dimension = null;
        if (name.matches("\\d+")) {
            // anonymous dimension
            dimension = new Dimension();
            dimension.setIsShared(false);
            dimension.setIsUnlimited(false);
            dimension.setIsVariableLength(false);
            dimension.setLength(name);
            return dimension;
        }
        AbstractAttributeContainer parent = this.parent;
        do {
            for (Dimension candidate : parent.getDimensions()) {
                if (name.equals(candidate.getName())) {
                    return candidate;
                }
            }
        } while ((parent = parent.parent) != null);
        // dimension is not declared; this cannot happen with ncdump -h -x
        throw new IllegalArgumentException("Not a valid dimension in schema: " + name);
    }

    @Override
    protected List<Attribute> getAttributesImpl() {
        return variable.getAttribute();
    }

    @Override
    public String getName() {
        return name;
    }

}
