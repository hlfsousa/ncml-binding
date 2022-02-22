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

import java.util.ArrayList;
import java.util.List;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.converters.ArrayNumberConverter;
import io.github.hlfsousa.ncml.io.converters.ArrayStringConverter;
import io.github.hlfsousa.ncml.io.converters.ScalarCharConverter;
import io.github.hlfsousa.ncml.io.converters.ScalarNumberConverter;
import io.github.hlfsousa.ncml.io.converters.ScalarStringConverter;
import io.github.hlfsousa.ncml.io.converters.VLenNumberConverter;
import ucar.ma2.Array;

/**
 * Converts to and from NetCDF arrays.
 *
 * @author Henrique Sousa
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConvertUtils {

    private static final ConvertUtils INSTANCE = new ConvertUtils();
    
    static {
        // default converters
        INSTANCE.register(new VLenNumberConverter());
        INSTANCE.register(new ScalarNumberConverter());
        INSTANCE.register(new ArrayNumberConverter());
        INSTANCE.register(new ScalarStringConverter());
        INSTANCE.register(new ArrayStringConverter());
        INSTANCE.register(new ScalarCharConverter());
    }

    public static ConvertUtils getInstance() {
        return INSTANCE;
    }

    private final List<Converter> registry = new ArrayList<>();

    public <T> void register(Converter<T> converter) {
        registry.add(converter);
    }
    
    public Array toArray(Object value, CDLAttribute attributeDecl) {
        for (Converter converter : registry) {
            if (converter.isApplicable(value, attributeDecl)) {
                return converter.toArray(value, attributeDecl);
            }
        }
        throw new IllegalArgumentException("Unable to convert " + value + " to " + attributeDecl + ", no converter registered");
    }

    public Array toArray(Object value, CDLVariable variableDecl) {
        for (Converter converter : registry) {
            if (converter.isApplicable(value, variableDecl)) {
                return converter.toArray(value, variableDecl);
            }
        }
        throw new IllegalArgumentException("Unable to convert " + value + " to " + variableDecl
                + ", no converter registered");
    }

    public <T> T toJavaObject(Array array, Class<T> toType) {
        for (Converter converter : registry) {
            if (converter.isApplicable(array, toType)) {
                return (T) converter.toJavaObject(array, toType);
            }
        }
        throw new IllegalArgumentException(
                "Unable to convert " + array + " to " + toType + ", no converter registered");
    }

}
