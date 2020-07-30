package hsousa.ncml.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a class or interface as the root of a CDL structure.
 * 
 * @author Henrique Sousa
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface CDLRoot {

}