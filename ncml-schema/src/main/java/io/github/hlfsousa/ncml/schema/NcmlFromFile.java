package io.github.hlfsousa.ncml.schema;

/*-
 * #%L
 * ncml-schema
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

import java.util.ArrayList;
import java.util.List;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.Variable;
import ucar.ma2.IndexIterator;
import ucar.nc2.NetcdfFile;

public class NcmlFromFile {

    public Netcdf extractHeader(NetcdfFile netcdfFile) {

        Netcdf netcdf = new Netcdf();
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readAttributes(netcdfFile.getRootGroup()));
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readDimensions(netcdfFile.getRootGroup()));
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readVariables(netcdfFile.getRootGroup()));
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readGroups(netcdfFile.getRootGroup()));

        return netcdf;
    }

    private List<Dimension> readDimensions(ucar.nc2.Group group) {
        return new ArrayList<>();
    }

    private List<Variable> readVariables(ucar.nc2.Group group) {
        return new ArrayList<>();
    }

    private List<Group> readGroups(ucar.nc2.Group parentGroup) {
        ArrayList<Group> groups = new ArrayList<>();
        for (ucar.nc2.Group ncGroup : parentGroup.getGroups()) {
            Group group = new Group();
            groups.add(group);
            group.getEnumTypedefOrDimensionOrVariable().addAll(readAttributes(ncGroup));
            group.getEnumTypedefOrDimensionOrVariable().addAll(readDimensions(ncGroup));
            group.getEnumTypedefOrDimensionOrVariable().addAll(readVariables(ncGroup));
            group.getEnumTypedefOrDimensionOrVariable().addAll(readGroups(ncGroup));
        }
        return groups;
    }

    private List<Attribute> readAttributes(ucar.nc2.Group group) {
        List<Attribute> attributes = new ArrayList<>();
        for (ucar.nc2.Attribute ncAttribute : group.attributes()) {
            Attribute attribute = new Attribute();
            attribute.setIsUnsigned(ncAttribute.getDataType().isUnsigned());
            attribute.setType(ncAttribute.getDataType().toString());
            if (ncAttribute.getValues() != null && ncAttribute.getValues().getSize() > 0) {
                if (!ncAttribute.getDataType().isUnsigned()) {
                    attribute.setValue(ncAttribute.getValues().toString());
                } else {
                    StringBuilder str = new StringBuilder();
                    IndexIterator idxIterator = ncAttribute.getValues().getIndexIterator();
                    while (idxIterator.hasNext()) {
                        if (str.length() > 0) {
                            str.append(' ');
                        }
                        Long item = idxIterator.getLongNext();
                        str.append(item == null ? null : Long.toUnsignedString(item));
                    }
                }
            }
        }
        return attributes;
    }

}
