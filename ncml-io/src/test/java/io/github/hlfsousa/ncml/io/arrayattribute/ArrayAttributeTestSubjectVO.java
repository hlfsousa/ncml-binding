package io.github.hlfsousa.ncml.io.arrayattribute;

public class ArrayAttributeTestSubjectVO implements ArrayAttributeTestSubject {
    // additionalFields >>
    // << additionalFields
    private int[] arrayAttribute;

    @Override
    public int[] getArrayAttribute() {
        return arrayAttribute;
    }

    @Override
    public void setArrayAttribute(int[] arrayAttribute) {
        this.arrayAttribute = arrayAttribute;
    }

    private String strAttribute;

    @Override
    public String getStrAttribute() {
        return strAttribute;
    }

    @Override
    public void setStrAttribute(String strAttribute) {
        this.strAttribute = strAttribute;
    }

    // additionalMethods >>
    // << additionalMethods

}
