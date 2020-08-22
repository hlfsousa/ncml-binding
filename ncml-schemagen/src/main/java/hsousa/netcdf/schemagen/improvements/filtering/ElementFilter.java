package hsousa.netcdf.schemagen.improvements.filtering;

import java.util.List;

public interface ElementFilter<T> {

    List<T> apply(List<T> variable);

}
