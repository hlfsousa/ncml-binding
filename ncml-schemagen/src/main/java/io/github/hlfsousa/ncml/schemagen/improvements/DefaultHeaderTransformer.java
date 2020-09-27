package io.github.hlfsousa.ncml.schemagen.improvements;

import java.util.Arrays;
import java.util.List;

import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Variable;
import io.github.hlfsousa.ncml.schemagen.improvements.filtering.ElementFilter;
import io.github.hlfsousa.ncml.schemagen.improvements.filtering.GroupArchetypeFilter;
import io.github.hlfsousa.ncml.schemagen.improvements.filtering.VariableAttributeFilter;

public class DefaultHeaderTransformer extends HeaderTransformer {

    private static final ElementFilter<Variable> VAR_MAP_UNIT_LONGNAME = new VariableAttributeFilter()
            .withCommonValue("units")
            .withNameBasedOn("long_name")
            .withSequentialMatching(true);
    private static final ElementFilter<Group> GROUP_MAP = new GroupArchetypeFilter();

    @Override
    protected List<ElementFilter<Group>> getGroupFilters() {
        return Arrays.asList(GROUP_MAP);
    }

    @Override
    protected List<ElementFilter<Variable>> getVariableFilters() {
        return Arrays.asList(VAR_MAP_UNIT_LONGNAME);
    }

}
