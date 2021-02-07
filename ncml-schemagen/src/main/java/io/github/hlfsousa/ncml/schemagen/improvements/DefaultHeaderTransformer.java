package io.github.hlfsousa.ncml.schemagen.improvements;

/*-
 * #%L
 * ncml-schemagen
 * %%
 * Copyright (C) 2020 - 2021 Henrique L. F. de Sousa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

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
