package io.github.hlfsousa.ncml.io;

import java.util.function.Consumer;

/**
 * Represents an operation that accepts three input arguments and returns no result.
 *
 * @param <A> the type of the first argument to the operation
 * @param <B> the type of the second argument to the operation
 * @param <C> the type of the third argument to the operation
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(A a, B b, C c);

}
