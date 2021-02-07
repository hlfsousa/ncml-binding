package io.github.hlfsousa.ncml.schema;

/*-
 * #%L
 * ncml-schema
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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import edu.ucar.unidata.netcdf.ncml.Attribute;
import edu.ucar.unidata.netcdf.ncml.Group;
import edu.ucar.unidata.netcdf.ncml.Netcdf;

/**
 * Utility class for frequent operations on schema classes.
 * 
 * @author Henrique Sousa
 */
public class NcmlSchemaUtil {
    
    private NcmlSchemaUtil() { }

    private static Map<Pattern, String> TYPE_REGEX;
    
    static {
        Map<Pattern, String> typeMapping = new LinkedHashMap<>();
        typeMapping.put(Pattern.compile("-?\\d+"), "long");
        typeMapping.put(Pattern.compile("-?(?:\\d+)?\\.\\d+|"), "double");
        TYPE_REGEX = Collections.unmodifiableMap(typeMapping);
    }

    /**
     * Retrieves a map of a selected type of element from a list of child elements. Most child elements, regardless of
     * type, from {@link Netcdf} and {@link Group} are provided as a list of {@link Object}, so we always have to filter
     * them. We also usually want the elements by name, so putting them in a map makes location easier.
     * 
     * @param <T>        the type of element to be retrieved
     * @param childList  the list of elements
     * @param targetType the type of element to be retrieved
     * @param keyMapper  a function that returns the name of the element
     * @return a map containing only child elements of the selected type with the name (as per {@code keyMapper}) as key
     */
    public static <T> Map<String, T> mapChildren(final List<?> childList, Class<T> targetType, Function<T, String> keyMapper) {
        @SuppressWarnings("unchecked")
        Map<String, T> children = childList.stream()
                .filter(child -> targetType.isInstance(child))
                .map(child -> (T) child)
                .collect(Collectors.toMap(keyMapper, child -> child));
        return children;
    }
    
    public static String inferAttributeType(Attribute attribute) {
        String type = attribute.getType();
        if (type == null || type.isEmpty()) {
            type = inferTypeFromValue(attribute.getValue());
        }
        return type;
    }
    
    public static String inferTypeFromValue(String value) {
        String type = "string";
        if (value != null) {
            for (Entry<Pattern, String> mapping : TYPE_REGEX.entrySet()) {
                if (mapping.getKey().matcher(value).matches()) {
                    type = mapping.getValue();
                    break;
                }
            }
        }
        return type;
    }

}
