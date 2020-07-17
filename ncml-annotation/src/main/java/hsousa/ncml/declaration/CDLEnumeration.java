package hsousa.ncml.declaration;

/**
 * A CDL enumeration. Enumerations map numbers to tokens.
 *
 * @author Henrique Sousa
 *
 * @param <T> the number type that maps values: Byte, Integer, or Long
 */
public interface CDLEnumeration<T extends Number> {

    /**
     * @return the mapping key.
     */
    T getKey();

    /**
     * @return the mapped token
     */
    String getValue();

}
