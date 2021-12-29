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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.converters.ArrayNumberConverter;
import io.github.hlfsousa.ncml.io.converters.ArrayStringConverter;
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
        INSTANCE.register(Object.class, new VLenNumberConverter());
        INSTANCE.register(Number.class, new ScalarNumberConverter());
        INSTANCE.register(Object.class, new ArrayNumberConverter());
        INSTANCE.register(String.class, new ScalarStringConverter());
        INSTANCE.register(Object.class, new ArrayStringConverter());
    }

    public static ConvertUtils getInstance() {
        return INSTANCE;
    }

    private final Map<Class<?>, List<Converter>> registry = new LinkedHashMap<>();

    public <T> void register(Class<T> type, Converter<T> converter) {
        registry.computeIfAbsent(type, key -> new ArrayList<>()).add(converter);
    }
    
    public Array toArray(Object value, CDLAttribute attributeDecl) {
        for (Entry<Class<?>, List<Converter>> entry : registry.entrySet()) {
            if (entry.getKey().isInstance(value)) {
                for (Converter converter : entry.getValue()) {
                    if (converter.isApplicable(value)) {
                        return converter.toArray(value, attributeDecl);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Unable to convert " + value + " to Array, no converter registered");
    }

    public Array toArray(Object value, CDLVariable variableDecl) {
        for (Entry<Class<?>, List<Converter>> entry : registry.entrySet()) {
            if (entry.getKey().isInstance(value)) {
                for (Converter converter : entry.getValue()) {
                    if (converter.isApplicable(value)) {
                        return converter.toArray(value, variableDecl);
                    }
                }
            }
        }
        throw new IllegalArgumentException("Unable to convert " + value + " to Array, no converter registered");
    }

    public <T> T toJavaObject(Array array, Class<T> toType) {
        for (Entry<Class<?>, List<Converter>> entry : registry.entrySet()) {
            if (entry.getKey().isAssignableFrom(toType)) {
                for (Converter converter : entry.getValue()) {
                    if (converter.isApplicable(array)) {
                        return (T) converter.toJavaObject(array, toType);
                    }
                }
            }
        }
        throw new IllegalArgumentException(
                "Unable to convert " + array + " to " + toType + ", no converter registered");
    }

}
