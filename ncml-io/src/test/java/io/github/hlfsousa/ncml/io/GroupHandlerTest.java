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
import static org.hamcrest.Matchers.notNullValue;
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

import io.github.hlfsousa.ncml.io.read.GroupHandler;
import ucar.ma2.Array;
import ucar.ma2.ArrayString;
import ucar.ma2.Index;
import ucar.nc2.Attribute;
import ucar.nc2.Group;
import ucar.nc2.Variable;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GroupHandlerTest {

    private static final String DESCRIPTION_ATTRIBUTE_VALUE = "description attribute value";
    @Mock
    private Group rootGroup;
    @Mock
    private Group childGroup;
    @Mock
    private Attribute description;
    @Mock
    private Variable someVariable;
    
    private RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration(Collections.emptyMap());

    @BeforeEach
    public void setupNetcdf() throws Exception {
        when(rootGroup.findGroup("child_group")).thenReturn(childGroup);
        when(rootGroup.getFullName()).thenReturn("");
        when(childGroup.getShortName()).thenReturn("child_group");
        when(childGroup.getFullName()).thenReturn("child_group");
        when(rootGroup.findAttribute("description")).thenReturn(description);
        Array attributeValues = Array.factory(String.class, Index.scalarIndexImmutable.getShape());
        attributeValues.setObject(Index.scalarIndexImmutable, DESCRIPTION_ATTRIBUTE_VALUE);
        when(description.getValues()).thenReturn(attributeValues);
        when(description.getFullName()).thenReturn("description");
        when(childGroup.findVariable("some_variable")).thenReturn(someVariable);
        when(someVariable.getFullName()).thenReturn("child_group/some_variable");
    }

    @Test
    public void testGetGroup() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        TestFile testFile = (TestFile) Proxy.newProxyInstance(classLoader,
                new Class<?>[] { TestFile.class }, new GroupHandler(rootGroup, true, runtimeConfiguration));
        assertThat(testFile.getChildGroup(), is(notNullValue()));
        assertTrue(testFile.getChildGroup() == testFile.getChildGroup(), "Each call returns a different instance");
    }

    @Test
    public void testGetVariable() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ChildGroup child = (ChildGroup) Proxy.newProxyInstance(classLoader,
                new Class<?>[] { ChildGroup.class }, new GroupHandler(childGroup, true, runtimeConfiguration));
        assertThat(child.getSomeVariable(), is(notNullValue()));
        assertTrue(child.getSomeVariable() == child.getSomeVariable(), "Each call returns a different instance");
    }

    @Test
    public void testGetAttribute() {
        TestFile testFile = (TestFile) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[] { TestFile.class },
                new GroupHandler(rootGroup, true, runtimeConfiguration));
        assertThat(testFile.getDescription(), is(DESCRIPTION_ATTRIBUTE_VALUE));
    }

}
