package hsousa.ncml.io.wrapper;

import java.io.IOException;
import java.util.function.BiFunction;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;
import ucar.nc2.Attribute;
import ucar.nc2.Group;
import ucar.nc2.Variable;

/**
 * Wraps a group, granting access to the variables and attributes it contains.
 * 
 * @author Henrique Sousa
 */
public abstract class NetcdfWrapper {

    protected final Group group;
    
    public NetcdfWrapper(Group group) {
        this.group = group;
    }
    
    /**
     * Applies <a href="https://www.unidata.ucar.edu/software/netcdf/docs/attribute_conventions.html">attribute
     * conventions</a> to an array value being read.
     * 
     * @param rawArray       the array with raw values
     * @param scaleFactor value of the scale_factor attribute
     * @param addOffset   value of the add_offset attribute
     * @param missingValue value of the missing_value attribute
     * @return adjusted array, with a data type compatible with the scale factor
     */
    protected Array rawToScaled(Array rawArray, Number scaleFactor, Number addOffset, Number missingValue) {
        Array scaled = Array.factory(DataType.getType(scaleFactor.getClass(), false), rawArray.getShape());
        if (addOffset == null) {
            addOffset = 0d;
        }
        for (IndexIterator readIt = rawArray.getIndexIterator(), writeIt = scaled.getIndexIterator(); readIt.hasNext(); ) {
            double rawValue = readIt.getDoubleNext();
            if (missingValue != null && rawValue == missingValue.doubleValue()) {
                writeIt.setDoubleNext(Double.NaN);
            } else {
                writeIt.setDoubleNext(rawValue * scaleFactor.doubleValue() + addOffset.doubleValue());
            }
        }
        return scaled;
    }
    
    protected Array getNumericArray(String shortName) {
        Variable variable = group.findVariable(shortName);
        if (variable == null) {
            return null;
        }
        try {
            Array value = variable.read();
            Number missingValue = null;
            Attribute missingValueAttribute = variable.findAttribute("missing_value");
            if (missingValueAttribute != null) {
                assert !missingValueAttribute.isString() : "missing_value must be numeric";
                missingValue = missingValueAttribute.getNumericValue();
            }
            Attribute scaleFactorAttribute = variable.findAttribute("scale_factor");
            if (value != null && scaleFactorAttribute != null) {
                assert !scaleFactorAttribute.isString() : "scale_factor must be numeric";
                Number scaleFactor = scaleFactorAttribute.getNumericValue();
                Number addOffset = 0d;
                Attribute addOffsetAttribute = variable.findAttribute("add_offset");
                if (addOffsetAttribute != null) {
                    assert !addOffsetAttribute.isString() : "add_offset must be numeric";
                    addOffset = addOffsetAttribute.getNumericValue();
                }
                value = rawToScaled(value, scaleFactor, addOffset, missingValue);
            }
            return value;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read variable " + shortName);
        }
    }
    
}
