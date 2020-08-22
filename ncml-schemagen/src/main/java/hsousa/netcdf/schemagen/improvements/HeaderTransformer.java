package hsousa.netcdf.schemagen.improvements;

import static java.util.stream.Collectors.toList;

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
        //filterGroups(childElements);
        filterVariables(childElements);
        
        return schema;
    }

    private void filterVariables(List<Object> childElements) {
        List<Object> otherElements = new ArrayList<>();
        List<Variable> variableList = new ArrayList<>();
        for (Object element : childElements) {
            if (element instanceof Variable) {
                variableList.add((Variable) element);
            } else {
                otherElements.add(element);
            }
        }
        for (ElementFilter<Variable> filter : getVariableFilters()) {
            variableList = filter.apply(variableList);
        }
        childElements.clear();
        childElements.addAll(otherElements);
        childElements.addAll(variableList);
    }

    private <T> List<T> listElements(List<Object> elementList, Class<T> type) {
        return elementList.stream()
                .filter(element -> type.isInstance(element))
                .map(element -> (T) element)
                .collect(toList());
    }
    
}
