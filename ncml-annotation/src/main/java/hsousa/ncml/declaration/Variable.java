package hsousa.ncml.declaration;

public interface Variable<T> {

    T getValue();

    void setValue(T value);

}
