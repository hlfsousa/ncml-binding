package hsousa.ncml.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Declares an attribute.
 *
 * @author Henrique Sousa
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface CDLAttribute {

    /**
     * Attribute name. Defaults to the field name.
     *
     * @return attribute name
     */
    String name() default "";

    /**
     * Optional separator for multi-valued attributes.
     *
     * @return value separator (optional)
     */
    String separator() default "";

    /**
     * Optional data type, if not to be derived from the field.
     *
     * @return data type
     */
    String dataType() default "string";

    boolean unsigned() default false;
    
    String defaultValue() default "";

}
