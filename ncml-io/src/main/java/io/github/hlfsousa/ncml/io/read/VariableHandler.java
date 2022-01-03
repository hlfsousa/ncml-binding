package io.github.hlfsousa.ncml.io.read;

/*-
 * #%L
 * ncml-io
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

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.io.AttributeConventions;
import io.github.hlfsousa.ncml.io.RuntimeConfiguration;
import ucar.ma2.Array;
import ucar.nc2.Variable;

public class VariableHandler extends AbstractCDMNodeHandler<Variable> implements InvocationHandler {

    private static final AttributeConventions attributeConventions = new AttributeConventions();

    private final Map<Method, Object> invocationCache = new HashMap<>();
    private Type valueType;

    public VariableHandler(Variable variable, Type valueType, boolean readOnly,
            RuntimeConfiguration runtimeProperties) {
        super(variable, readOnly, runtimeProperties);
        this.valueType = valueType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!isFromInterface(proxy, method)) {
            if (method.equals(TO_STRING)) {
                return stringValue();
            }
            return callActualImplementation(proxy, method, args);
        }

        InvocationType methodType = InvocationType.of(method);
        String key = methodType.getKey(method);
        switch (methodType) {
        case ACCESSOR:
            // is dirty?
            if (values.containsKey(key)) {
                return values.get(key);
            }
            // was called previously?
            if (invocationCache.containsKey(method)) {
                return invocationCache.get(method);
            }
            break;
        case MODIFIER:
            // TODO mapped values?
            values.put(key, args[0]);
            return null;
        default:
            throw new IllegalStateException("Unsupported method type " + methodType + " -- " + method);
        }

        Object result;
        if (method.isAnnotationPresent(CDLAttribute.class)) {
            result = getAttribute(method);
        } else if (method.getDeclaringClass().equals(io.github.hlfsousa.ncml.declaration.Variable.class)) {
            if (method.getName().equals("getValue")) {
                result = getVariableValue(method);
            } else if (method.getName().equals("getDimensions")) {
                result = node == null ? null : node.getDimensions();
            } else {
                throw new UnsupportedOperationException("No super/default calls yet: " + method);
            }
        } else {
            throw new UnsupportedOperationException("No super/default calls yet: " + method);
        }
        invocationCache.put(method, result);
        return result;
    }

    private Object getVariableValue(Method method) throws IOException {
        if (node == null)
            return null;
        Array arrayValue = attributeConventions.readNumericArray(node);
        Class<?> expectedType;
        if (valueType instanceof ParameterizedType) {
            expectedType = (Class<?>) ((ParameterizedType) valueType).getActualTypeArguments()[0];
        } else {
            expectedType = (Class<?>) valueType;
        }
        if (expectedType.isAssignableFrom(Array.class)) {
            return arrayValue;
        }
        return convertUtils.toJavaObject(arrayValue, expectedType);
    }

}
