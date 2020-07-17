package hsousa.ncml.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declaration of a CDL dimension.
 *
 * @author Henrique Sousa
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface CDLDimension {

    String name();

    int length() default 0;

    boolean unlimited() default false;

    boolean variableLength() default false;

}
