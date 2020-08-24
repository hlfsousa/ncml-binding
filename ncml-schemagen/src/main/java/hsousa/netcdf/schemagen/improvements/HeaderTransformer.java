package hsousa.netcdf.schemagen.improvements;

import java.util.ArrayList;
import java.util.List;

import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;
import hsousa.netcdf.schemagen.improvements.filtering.ElementFilter;

public abstract class HeaderTransformer {

    protected abstract List<ElementFilter<Group>> getGroupFilters();
    protected abstract List<ElementFilter<Variable>> getVariableFilters();
    
    public Netcdf modify(Netcdf schema) {
        List<Object> childElements = schema.getEnumTypedefOrGroupOrDimension();
        filterElements(Variable.class, childElements, getVariableFilters());
        filterElements(Group.class, childElements, getGroupFilters());
        return schema;
    }
    
    @SuppressWarnings("unchecked")
    private <T> void filterElements(Class<T> type, List<Object> childElements, List<ElementFilter<T>> filterList) {
        List<Object> otherElements = new ArrayList<>();
        List<T> selectedElements = new ArrayList<>();
        for (Object element : childElements) {
            if (type.isInstance(element)) {
                selectedElements.add((T) element);
            } else {
                otherElements.add(element);
            }
        }
        for (ElementFilter<T> filter : filterList) {
            selectedElements = filter.apply(selectedElements);
        }
        childElements.clear();
        childElements.addAll(otherElements);
        childElements.addAll(selectedElements);
    }

}
