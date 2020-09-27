package io.github.hlfsousa.ncml.schemagen.improvements.filtering;

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
