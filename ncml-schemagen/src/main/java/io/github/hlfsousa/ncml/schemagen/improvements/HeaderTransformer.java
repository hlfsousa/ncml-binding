package io.github.hlfsousa.ncml.schemagen.improvements;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;
import io.github.hlfsousa.ncml.schemagen.improvements.filtering.ElementFilter;

public abstract class HeaderTransformer {

    protected abstract List<ElementFilter<Group>> getGroupFilters();
    protected abstract List<ElementFilter<Variable>> getVariableFilters();
    
    public Netcdf modify(Netcdf schema, Properties properties) {
        List<Object> childElements = schema.getEnumTypedefOrGroupOrDimension();
        filterElements(properties, Variable.class, childElements, getVariableFilters());
        filterElements(properties, Group.class, childElements, getGroupFilters());
        return schema;
    }
    
    @SuppressWarnings("unchecked")
    private <T> void filterElements(Properties properties, Class<T> type, List<Object> childElements,
            List<ElementFilter<T>> filterList) {
        List<Object> otherElements = new ArrayList<>();
        List<T> selectedElements = new ArrayList<>();
        for (Object element : childElements) {
            if (type.isInstance(element)) {
                selectedElements.add((T) element);
            } else {
                otherElements.add(element);
            }
            // recurse to groups first
            if (element instanceof Group) {
                filterElements(properties, type, ((Group) element).getEnumTypedefOrDimensionOrVariable(), filterList);
            }
        }
        for (ElementFilter<T> filter : filterList) {
            selectedElements = filter.apply(selectedElements, properties);
        }
        childElements.clear();
        childElements.addAll(otherElements);
        childElements.addAll(selectedElements);
    }

}
