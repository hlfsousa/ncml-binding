package io.github.hlfsousa.ncml.annotation;

/*-
 * #%L
 * ncml-annotation
 * %%
 * Copyright (C) 2020 - 2021 Henrique L. F. de Sousa
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.github.hlfsousa.ncml.declaration.CDLEnumeration;

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
