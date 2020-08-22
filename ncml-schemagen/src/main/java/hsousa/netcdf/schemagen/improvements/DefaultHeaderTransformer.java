package hsousa.netcdf.schemagen.improvements;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Variable;
import hsousa.netcdf.schemagen.improvements.filtering.ElementFilter;
import hsousa.netcdf.schemagen.improvements.filtering.VariableAttributeFilter;

public class DefaultHeaderTransformer extends HeaderTransformer {

    private static final ElementFilter<Variable> VAR_MAP_UNIT_LONGNAME = new VariableAttributeFilter()
            .withCommonValue("units")
            .withNameBasedOn("long_name")
            .withSequentialMatching(true);

    @Override
    protected List<ElementFilter<Group>> getGroupFilters() {
        return Collections.emptyList();
    }

    @Override
    protected List<ElementFilter<Variable>> getVariableFilters() {
        return Arrays.asList(VAR_MAP_UNIT_LONGNAME);
    }

}
