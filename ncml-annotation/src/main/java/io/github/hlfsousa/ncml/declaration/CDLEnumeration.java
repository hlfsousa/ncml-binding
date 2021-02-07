package io.github.hlfsousa.ncml.declaration;

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
