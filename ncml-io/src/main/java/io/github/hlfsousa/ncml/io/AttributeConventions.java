package io.github.hlfsousa.ncml.io;

import java.io.IOException;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

/**
 * Applies <a href="https://www.unidata.ucar.edu/software/netcdf/docs/attribute_conventions.html">attribute
 * conventions</a> to an array value being read or written. If a different convention for the attribute names is used,
 * refer to {@link #AttributeConventions(AttributeConventionsAttributes)}.
 */
public class AttributeConventions {

    /**
     * Provides the names of attributes that provide the pieces of information needed for variable scaling.
     */
    public static interface AttributeConventionsAttributes {

        String getMissingValueAttributeName();

        String getScaleFactorAttributeName();

        String getAddOffsetAttributeName();

    }

    /**
     * Provides the default names for the attributes used for scaling. See class description for details.
     */
    public static final class DefaultAttributeConventionsAttributes implements AttributeConventionsAttributes {

        private final String missingValueAttributeName = "missing_value";
        private final String scaleFactorAttributeName = "scale_factor";
        private final String addOffsetAttributeName = "add_offset";

        public String getMissingValueAttributeName() {
            return missingValueAttributeName;
        }

        public String getScaleFactorAttributeName() {
            return scaleFactorAttributeName;
        }

        public String getAddOffsetAttributeName() {
            return addOffsetAttributeName;
        }

    }

    /**
     * Provides the actual transformation, given the attributes involved in scaling.
     */
    public static enum ArrayScaling {
        TO_RAW {
            @Override
            public Array transform(Array scaledArray, Number scaleFactor, Number addOffset, Number missingValue) {
                Array rawArray = Array.factory(
                        missingValue != null ? DataType.getType(missingValue.getClass(), false) : DataType.SHORT,
                        scaledArray.getShape());
                for (IndexIterator writeIt = rawArray.getIndexIterator(), readIt = scaledArray.getIndexIterator();
                        readIt.hasNext();) {
                    double scaledValue = readIt.getDoubleNext();
                    if (missingValue != null && Double.isNaN(scaledValue)) {
                        writeIt.setShortNext(missingValue.shortValue());
                    } else {
                        writeIt.setIntNext((int) Math.round(
                                (scaledValue - addOffset.doubleValue()) / scaleFactor.doubleValue()));
                    }
                }
                return rawArray;
            }
        },
        TO_SCALED {
            @Override
            public Array transform(Array rawArray, Number scaleFactor, Number addOffset, Number missingValue) {
                Array scaledArray = Array.factory(DataType.getType(scaleFactor.getClass(), false), rawArray.getShape());
                for (IndexIterator readIt = rawArray.getIndexIterator(), writeIt = scaledArray.getIndexIterator();
                        readIt.hasNext();) {
                    double rawValue = readIt.getDoubleNext();
                    if (missingValue != null && rawValue == missingValue.doubleValue()) {
                        writeIt.setDoubleNext(Double.NaN);
                    } else {
                        writeIt.setDoubleNext(rawValue * scaleFactor.doubleValue() + addOffset.doubleValue());
                    }
                }
                return scaledArray;
            }
        };

        public abstract Array transform(Array arrayValue, Number scaleFactor, Number addOffset, Number missingValue);

    }

    private static final AttributeConventionsAttributes DEFAULT_CONVENTION = new DefaultAttributeConventionsAttributes();

    private static AttributeConventionsAttributes getDefaultConvention() {
        return DEFAULT_CONVENTION;
    }

    private final AttributeConventionsAttributes convention;

    public AttributeConventions() {
        this(getDefaultConvention());
    }

    public AttributeConventions(AttributeConventionsAttributes convention) {
        this.convention = convention;
    }

    /**
     * Retrieves the value from a variable and scales it if its attributes indicate that necessity. If the
     * attributes do not indicate scaling, no transformation is performed.
     *
     * @param variable variable whose value to retrieve
     * @param value the value to be transformed (optional if reading the variable)
     * @param scaling type of scaling to perform if necessary
     * @return array value after scaling
     */
    public Array transformVariableValue(Variable variable, Array value, ArrayScaling scaling) {
        try {
            if (value == null) {
                value = variable.read();
            }
            Number missingValue = null;
            Attribute missingValueAttribute = variable.findAttribute(convention.getMissingValueAttributeName());
            if (missingValueAttribute != null) {
                assert !missingValueAttribute.isString() : convention.getMissingValueAttributeName()
                        + " must be numeric";
                missingValue = missingValueAttribute.getNumericValue();
            }
            Attribute scaleFactorAttribute = variable.findAttribute(convention.getScaleFactorAttributeName());
            if (value != null && scaleFactorAttribute != null) {
                assert !scaleFactorAttribute.isString() : convention.getMissingValueAttributeName()
                        + " must be numeric";
                Number scaleFactor = scaleFactorAttribute.getNumericValue();
                Number addOffset = 0d;
                Attribute addOffsetAttribute = variable.findAttribute(convention.getAddOffsetAttributeName());
                if (addOffsetAttribute != null) {
                    assert !addOffsetAttribute.isString() : convention.getMissingValueAttributeName()
                            + " must be numeric";
                    addOffset = addOffsetAttribute.getNumericValue();
                }
                value = scaling.transform(value, scaleFactor, addOffset, missingValue);
            }
            return value;
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read variable " + variable.getShortName());
        }
    }

    /**
     * Retrieves the array from a numeric variable, applying attribute conventions to scale it up if needed.
     * 
     * @param variable the variable being read
     * @return the potentially scaled up array value
     */
    public Array readNumericArray(Variable variable) {
        return transformVariableValue(variable, null, ArrayScaling.TO_SCALED);
    }

    /**
     * Retrieves the numeric array to be written from a numeric variable meant to be written.
     * 
     * @param variable the variable potentially being written
     * @param value the value to write to the variable
     * @return the raw array from the variable, scaled down if needed
     */
    public Array writeNumericArray(Variable variable, Array value) {
        return transformVariableValue(variable, value, ArrayScaling.TO_RAW);
    }

}