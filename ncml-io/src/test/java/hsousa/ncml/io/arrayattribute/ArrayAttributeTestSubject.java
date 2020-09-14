package hsousa.ncml.io.arrayattribute;

// imports >>
import hsousa.ncml.annotation.*;
import hsousa.ncml.declaration.*;
import java.util.*;
import ucar.ma2.Array;
// << imports

@CDLRoot
@CDLDimensions({
})
public interface ArrayAttributeTestSubject  {
    @CDLAttribute(name = "array_attribute", dataType = "int", defaultValue = "0 1 2 3 4 5 6 7 8 9")
    int[] getArrayAttribute();

    void setArrayAttribute(int[] arrayAttribute);

    @CDLAttribute(name = "str_attribute", dataType = "String", defaultValue = "foobar")
    String getStrAttribute();

    void setStrAttribute(String strAttribute);

    // methods >>
    // << methods

}
