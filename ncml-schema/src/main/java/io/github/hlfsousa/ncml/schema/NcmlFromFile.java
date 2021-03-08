package io.github.hlfsousa.ncml.schema;

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
        for (ucar.nc2.Attribute ncAttribute : group.getAttributes()) {
            Attribute attribute = new Attribute();
            attribute.setIsUnsigned(ncAttribute.isUnsigned());
            attribute.setType(ncAttribute.getDataType().toString());
            if (ncAttribute.getValues() != null && ncAttribute.getValues().getSize() > 0) {
                if (!ncAttribute.isUnsigned()) {
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
