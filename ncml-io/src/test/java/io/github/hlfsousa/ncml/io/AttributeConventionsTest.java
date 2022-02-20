package io.github.hlfsousa.ncml.io;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.io.AttributeConventions.ArrayScaling;
import ucar.ma2.Array;
import ucar.ma2.ArrayByte;
import ucar.ma2.ArrayInt;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

public class AttributeConventionsTest {

    private Variable mockVariable(DataType dataType, Double scaleFactor, Double addOffset, Number missingValue)
            throws IOException {
        Variable variable = mock(Variable.class);
        List<Attribute> attributes = new ArrayList<>();
        if (scaleFactor != null) {
            Attribute attribute = new Attribute("scale_factor", scaleFactor);
            attributes.add(attribute);
            when(variable.findAttribute("scale_factor")).thenReturn(attribute);
        }
        if (addOffset != null) {
            Attribute attribute = new Attribute("add_offset", addOffset);
            attributes.add(attribute);
            when(variable.findAttribute("add_offset")).thenReturn(attribute);
        }
        if (missingValue != null) {
            Attribute attribute = new Attribute("missing_value", missingValue);
            attributes.add(attribute);
            when(variable.findAttribute("missing_value")).thenReturn(attribute);
        }
        when(variable.getAttributes()).thenReturn(attributes);
        when(variable.getDataType()).thenReturn(dataType);
        when(variable.read()).then(invocation -> {
            throw new UnsupportedOperationException("invalid operation");
        });
        return variable;
    }
    
    @Test
    public void testScaleUnsigned() throws Exception {
        int max = 150;
        Random rng = new Random();
        int length = 50;
        int missing = 255;
        ArrayByte packed = new ArrayByte.D1(length);
        packed.setUnsigned(true);
        for (IndexIterator it = packed.getIndexIterator(); it.hasNext();) {
            it.next();
            it.setIntCurrent(it.getCurrentCounter()[0] % 10 == 0 ? missing : rng.nextInt(max));
        }
        
        double scaleFactor = 0.001;
        AttributeConventions attributeConventions = new AttributeConventions();
        Variable variable = mockVariable(DataType.BYTE, scaleFactor, 0d, (byte) missing);
        when(variable.isUnsigned()).thenReturn(true);
        Array scaled = attributeConventions.transformVariableValue(variable, packed, ArrayScaling.TO_SCALED);
        
        assertThat(scaled.getDataType(), is(DataType.DOUBLE));
        for (IndexIterator packedIt = packed.getIndexIterator(),
                scaledIt = scaled.getIndexIterator(); packedIt.hasNext(); ) {
            double scaledValue = scaledIt.getDoubleNext();
            int packedValue = packedIt.getIntNext();
            assertThat(String.format("packed: %s", packed),
                    scaledValue, is(packedValue == missing ? Double.NaN : packedValue * scaleFactor));
            if (!Double.isNaN(scaledValue)) {
                assertThat(String.format("packed: %s", packedValue), scaledValue, is(greaterThanOrEqualTo(0.)));
            }
        }
    }

    @Test
    public void testScale() throws Exception {
        int max = 100000;
        Random rng = new Random();
        int length = 50;
        int missing = -1;
        ArrayInt packed = new ArrayInt.D1(length);
        for (IndexIterator it = packed.getIndexIterator(); it.hasNext();) {
            it.next();
            it.setIntCurrent(it.getCurrentCounter()[0] % 10 == 0 ? missing : rng.nextInt(max));
        }

        double scaleFactor = 0.001;
        AttributeConventions attributeConventions = new AttributeConventions();
        Variable variable = mockVariable(DataType.INT, scaleFactor, 0d, missing);
        Array scaled = attributeConventions.transformVariableValue(variable, packed, ArrayScaling.TO_SCALED);

        assertThat(scaled.getDataType(), is(DataType.DOUBLE));
        for (IndexIterator packedIt = packed.getIndexIterator(),
                scaledIt = scaled.getIndexIterator(); packedIt.hasNext(); ) {
            double scaledValue = scaledIt.getDoubleNext();
            int packedValue = packedIt.getIntNext();
            assertThat(String.format("packed: %s", packed),
                    scaledValue, is(packedValue == missing ? Double.NaN : packedValue * scaleFactor));
        }
    }

}
