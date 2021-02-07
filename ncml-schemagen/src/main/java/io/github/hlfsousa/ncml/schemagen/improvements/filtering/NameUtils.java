package io.github.hlfsousa.ncml.schemagen.improvements.filtering;

/*-
 * #%L
 * ncml-schemagen
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

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

public final class NameUtils {
    
    private NameUtils() { }

    public static String reverse(String str) {
        StringBuilder reversed = new StringBuilder(str.length());
        for (int i = str.length() - 1; i >= 0; i--) {
            reversed.append(str.charAt(i));
        }
        return reversed.toString();
    }

    public static String findCommonSuffix(List<String> values) {
        List<String> reversed = values.stream().map(NameUtils::reverse).collect(toList());
        String reversedSuffix = findCommonPrefix(reversed);
        return reverse(reversedSuffix);
    }

    public static String findCommonPrefix(List<String> values) {
        Collections.sort(values);
        String first = values.get(0);
        String last = values.get(values.size() - 1);
        int limit = Math.min(first.length(), last.length());
        int end = 0;
        while (end < limit && first.charAt(end) == last.charAt(end)) {
            ++end;
        }
        return first.substring(0, end).trim();
    }

}
