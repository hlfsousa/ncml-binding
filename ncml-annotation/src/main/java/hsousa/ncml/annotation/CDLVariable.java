package hsousa.ncml.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import hsousa.ncml.declaration.CDLEnumeration;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface CDLVariable {

    String name() default "";

    /**
     * Variable type, which must be supported ({@link ucar.ma2.Array}, String, subclass of Number, or enumeration that
     * implements {@link CDLEnumeration}).
     */
    Class<?> type() default Object.class;

    boolean unsigned() default false;

    String[] shape() default {};

}
