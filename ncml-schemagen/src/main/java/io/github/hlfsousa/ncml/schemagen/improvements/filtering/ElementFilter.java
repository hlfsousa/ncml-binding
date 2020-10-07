package io.github.hlfsousa.ncml.schemagen.improvements.filtering;

import java.util.List;
import java.util.Properties;

public interface ElementFilter<T> {

    List<T> apply(List<T> elementList, Properties properties);

}
