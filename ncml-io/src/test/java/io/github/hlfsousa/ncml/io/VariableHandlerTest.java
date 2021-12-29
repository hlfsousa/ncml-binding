package io.github.hlfsousa.ncml.io;

/*-
 * #%L
 * ncml-io
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import io.github.hlfsousa.ncml.io.read.VariableHandler;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Index;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VariableHandlerTest {

    public static interface ScalarVariable extends io.github.hlfsousa.ncml.declaration.Variable<Double> {

    }

    @Mock
    private Variable variable;
    @Mock
    private Attribute longName;

    @Mock
    private Variable scalarVariable;

    private RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration(Collections.emptyMap());

    @BeforeEach
    public void setupMocks() throws Exception {
        when(variable.findAttribute("long_name")).thenReturn(longName);
        when(variable.getFullName()).thenReturn("variable");
        Array longNameValue = Array.factory(DataType.STRING, Index.scalarIndexImmutable.getShape());
        longNameValue.setObject(Index.scalarIndexImmutable, "some variable");
        when(longName.getValues()).thenReturn(longNameValue);

        when(scalarVariable.isScalar()).thenReturn(true);
        when(scalarVariable.read()).thenReturn(Array.factory(DataType.DOUBLE, new int[0], new double[] { 1.0 }));
    }

    @Test
    public void testVarAttributes() throws Exception {
        ChildGroupVariable variable = (ChildGroupVariable) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { ChildGroupVariable.class }, new VariableHandler(this.variable, Array.class, true, runtimeConfiguration));
        assertThat(variable.getLongName(), is("some variable"));
        assertTrue(variable.getLongName() == variable.getLongName());
    }

    @Test
    public void testScalarValue() throws Exception {
        ScalarVariable scalarVar = (ScalarVariable) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { ScalarVariable.class }, new VariableHandler(scalarVariable, Double.class, true, runtimeConfiguration));
        assertThat(scalarVar.getValue(), is(1.0));
    }

}
