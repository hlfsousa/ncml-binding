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
import java.util.Map.Entry;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Dimension;
import edu.ucar.unidata.netcdf.ncml.EnumTypedef;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;
import edu.ucar.unidata.netcdf.ncml.ObjectFactory;
import edu.ucar.unidata.netcdf.ncml.Variable;
import ucar.ma2.DataType.Signedness;
import ucar.ma2.IndexIterator;
import ucar.nc2.NetcdfFile;

/**
 * Extracts a NetCDF header as XML (NcML) from a file. Tool <a href="">ncgen</a> is only able to extract an XML header
 * from NetCDF classic model. Use this tool if {@code ncgen} is not a feasible way to get a header.
 * 
 * @author Henrique Sousa
 */
public class NcmlFromFile {

    private final ObjectFactory objectFactory = new ObjectFactory();
    
    /**
     * Does the extraction from file to XML (JAX-B classes). The result can be further customized before writing to
     * file.
     * 
     * @param netcdfFile the file from which to extract the XML header
     * @return the header
     */
    public Netcdf extractHeader(NetcdfFile netcdfFile) {

        Netcdf netcdf = new Netcdf();
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readEnumTypeDefs(netcdfFile.getRootGroup()));
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readAttributes(netcdfFile.getRootGroup().attributes()));
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readDimensions(netcdfFile.getRootGroup()));
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readVariables(netcdfFile.getRootGroup()));
        netcdf.getEnumTypedefOrGroupOrDimension().addAll(readGroups(netcdfFile.getRootGroup()));

        return netcdf;
    }

    private List<EnumTypedef> readEnumTypeDefs(ucar.nc2.Group group) {
        List<ucar.nc2.EnumTypedef> enumTypedefs = group.getEnumTypedefs();
        ArrayList<EnumTypedef> result = new ArrayList<>();
        for (ucar.nc2.EnumTypedef ncTypedef : enumTypedefs) {
            EnumTypedef typedef = new EnumTypedef();
            typedef.setName(ncTypedef.getShortName());
            typedef.setType(ncTypedef.getBaseType().toString());
            for (Entry<Integer, String> ncItem : ncTypedef.getMap().entrySet()) {
                EnumTypedef.Enum item = new EnumTypedef.Enum();
                item.setKey(ncItem.getKey());
                item.setContent(ncItem.getValue());
                typedef.getContent().add(objectFactory.createEnumTypedefEnum(item));
            }
        }
        return result;
    }

    private List<Dimension> readDimensions(ucar.nc2.Group group) {
        ArrayList<Dimension> dimensionList = new ArrayList<>();
        for (ucar.nc2.Dimension ncDimension : group.getDimensions()) {
            Dimension dimension = new Dimension();
            dimensionList.add(dimension);
            dimension.setName(ncDimension.getShortName());
            if (ncDimension.isUnlimited()) {
                dimension.setIsUnlimited(true);
            }
            if (ncDimension.isVariableLength()) {
                dimension.setIsVariableLength(true);
            } else {
                dimension.setLength(String.valueOf(ncDimension.getLength()));
            }
        }
        return dimensionList;
    }

    private List<Variable> readVariables(ucar.nc2.Group group) {
        ArrayList<Variable> variableList = new ArrayList<>();
        for (ucar.nc2.Variable ncVar : group.getVariables()) {
            ncVar.setCaching(false);
            Variable variable = new Variable();
            variableList.add(variable);
            variable.setName(ncVar.getShortName());
            variable.setType(ncVar.getDataType().withSignedness(Signedness.SIGNED).toString());
            variable.setShape(ncVar.getDimensionsString());
            variable.getAttribute().addAll(readAttributes(ncVar));
        }
        return variableList;
    }

    private List<Group> readGroups(ucar.nc2.Group parentGroup) {
        ArrayList<Group> groups = new ArrayList<>();
        for (ucar.nc2.Group ncGroup : parentGroup.getGroups()) {
            Group group = new Group();
            groups.add(group);
            group.setName(ncGroup.getShortName());
            group.getEnumTypedefOrDimensionOrVariable().addAll(readAttributes(ncGroup.attributes()));
            group.getEnumTypedefOrDimensionOrVariable().addAll(readDimensions(ncGroup));
            group.getEnumTypedefOrDimensionOrVariable().addAll(readVariables(ncGroup));
            group.getEnumTypedefOrDimensionOrVariable().addAll(readGroups(ncGroup));
            group.getEnumTypedefOrDimensionOrVariable().addAll(readEnumTypeDefs(ncGroup));
        }
        return groups;
    }

    private List<Attribute> readAttributes(Iterable<ucar.nc2.Attribute> ncAttributes) {
        List<Attribute> attributes = new ArrayList<>();
        for (ucar.nc2.Attribute ncAttribute : ncAttributes) {
            Attribute attribute = new Attribute();
            attribute.setName(ncAttribute.getShortName());
            attributes.add(attribute);
            if (ncAttribute.getDataType().isUnsigned()) {
                attribute.setIsUnsigned(true);
            }
            attribute.setType(ncAttribute.getDataType().withSignedness(Signedness.SIGNED).toString());
            if (ncAttribute.getValues() != null && ncAttribute.getValues().getSize() > 0) {
                if (!ncAttribute.getDataType().isUnsigned()) {
                    attribute.setValue(ncAttribute.getValues().toString().trim());
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
                    attribute.setValue(str.toString());
                }
            }
        }
        return attributes;
    }

}
