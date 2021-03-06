package io.github.hlfsousa.ncml.annotation;

/*-
 * #%L
 * ncml-annotation
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

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLDimension;
import io.github.hlfsousa.ncml.annotation.CDLDimensions;
import io.github.hlfsousa.ncml.annotation.CDLGroup;
import io.github.hlfsousa.ncml.annotation.CDLRoot;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.declaration.CDLEnumeration;
import io.github.hlfsousa.ncml.declaration.Variable;
import ucar.ma2.Array;

/**
 * It must be possible to annotate a class such that it describes an CDL schema. This test passes if the declarations
 * can be done and are retained in runtime, so most of the testing is done in compile/design time.
 *
 * @author Henrique Sousa
 */
public class AnnotationDeclarationTest {

    /**
     * CDL Enumerations map a number to a string and the number can be of type Byte (enum1), Short (enum2), or Integer
     * (enum4), which also limits how many enumerated values can be mapped.
     */
    public static enum ByteEnumeration implements CDLEnumeration<Byte> {
        Zero((byte) 0),
        Sixteen((byte) 16),
        Sixty((byte) 60);

        private byte key;

        private ByteEnumeration(byte key) {
            this.key = key;
        }

        @Override
        public Byte getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return name();
        }
    }

    /**
     * This describes the root group of the schema. The group name may actually differ from the class name, and that is
     * defined by the group property.
     */
    public static interface GroupOfAttributes {

        /**
         * Basic attribute declaration.
         */
        @CDLAttribute
        String getStringAttribute();

        /**
         * Attribute that maps to a different name in the schema.
         */
        @CDLAttribute(name = "notTheFieldName")
        String getCustomNameAttribute();

        /**
         * Attribute that maps to a list of values.
         */
        @CDLAttribute
        List<String> getListAttribute();

        /**
         * Attribute that maps to a list of values with a custom separator
         */
        @CDLAttribute(separator = "-")
        List<String> getCustomSeparator();

        @CDLAttribute(dataType = "xsd:token")
        List<String> getTokenList();

        @CDLAttribute
        Byte getByteAttribute();

        @CDLAttribute
        Character getCharAttribute();

        @CDLAttribute
        Short getShortAttribute();

        /**
         * Signed integer attribute.
         */
        @CDLAttribute
        Integer getIntAttribute();

        @CDLAttribute
        Long getLongAttribute();

        @CDLAttribute
        Float getFloatAttribute();

        // TODO Structure, opaque, unsigned

        @CDLAttribute
        ByteEnumeration getEnum1Attribute();

    }

    /**
     * A group that contains variables of several types. A variable may contain attributes.
     */
    @CDLDimensions({ //
            @CDLDimension(name = "dim1", length = 10), // predefined
            @CDLDimension(name = "dim2", unlimited = true) // unlimited
    })
    public static interface GroupOfVariables {

        /**
         * Scalar variable (single value)
         */
        @CDLVariable
        Integer getScalarNumber();

        /**
         * 1-D number variable (naked).
         */
        @CDLVariable(dataType = "int", shape = "dim1")
        Array getNumber1D();

        /** 2-D variable with attributes. */
        @CDLVariable(dataType = "byte", unsigned = true, shape = { "dim2", "dim3" }) // dim3 is inferred
        MyVariable<Array> getUbyte2D();

        /** Naked variable with anonymous dimension. */
        @CDLVariable(name = "anonDimension", dataType = "int", shape = "5")
        Array getFiveNumbers();

    }

    public static interface MyVariable<T> extends Variable<T> {

        @CDLAttribute
        String getLongName();

    }

    @CDLRoot
    @CDLDimension(name = "vlenDim", variableLength = true)
    public static interface RootGroup {

        @CDLAttribute
        String getDisclaimer();

        @CDLGroup
        GroupOfAttributes getAttributesGroup();

        @CDLGroup(name = "group_of_variables")
        GroupOfVariables getVariablesGroup();

    }

    @Test
    public void annotationsAreRuntime() throws Exception {
        Method disclaimer = RootGroup.class.getMethod("getDisclaimer");
        assertThat("@CDLRoot is not retained in runtime", RootGroup.class.isAnnotationPresent(CDLRoot.class), is(true));
        assertThat("@CDLAttribute not retained in runtime", disclaimer.isAnnotationPresent(CDLAttribute.class),
                is(true));
        assertThat("@CDLDimension not retained in runtime", RootGroup.class.isAnnotationPresent(CDLDimension.class),
                is(true));
        Method variable = GroupOfVariables.class.getMethod("getScalarNumber");
        assertThat("@CDLVariable not retained in runtime", variable.isAnnotationPresent(CDLVariable.class),
                is(true));
    }

}
