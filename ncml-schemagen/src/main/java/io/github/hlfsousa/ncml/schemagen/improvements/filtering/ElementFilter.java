package io.github.hlfsousa.ncml.schemagen.improvements.filtering;

import java.util.List;

public interface ElementFilter<T> {

    List<T> apply(List<T> elementList);

}
