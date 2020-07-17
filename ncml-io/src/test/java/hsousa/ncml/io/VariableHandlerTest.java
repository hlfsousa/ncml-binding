package hsousa.ncml.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import hsousa.ncml.io.read.VariableHandler;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VariableHandlerTest {

    public static interface ScalarVariable extends hsousa.ncml.declaration.Variable<Double> {

    }

    @Mock
    private Variable variable;
    @Mock
    private Attribute longName;

    @Mock
    private Variable scalarVariable;

    @BeforeEach
    public void setupMocks() throws Exception {
        when(variable.findAttribute("long_name")).thenReturn(longName);
        when(longName.getStringValue()).thenReturn("some variable");

        when(scalarVariable.isScalar()).thenReturn(true);
        when(scalarVariable.read()).thenReturn(Array.factory(DataType.DOUBLE, new int[0], new double[] { 1.0 }));
    }

    @Test
    public void testVarAttributes() throws Exception {
        ChildGroupVariable variable = (ChildGroupVariable) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { ChildGroupVariable.class }, new VariableHandler(this.variable, Array.class, true));
        assertThat(variable.getLongName(), is("some variable"));
        assertTrue(variable.getLongName() == variable.getLongName());
    }

    @Test
    public void testScalarValue() throws Exception {
        ScalarVariable scalarVar = (ScalarVariable) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { ScalarVariable.class }, new VariableHandler(scalarVariable, Double.class, true));
        assertThat(scalarVar.getValue(), is(1.0));
    }

}
