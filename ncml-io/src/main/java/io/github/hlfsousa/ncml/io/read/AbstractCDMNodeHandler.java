package io.github.hlfsousa.ncml.io.read;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.io.RuntimeConfiguration;
import io.github.hlfsousa.ncml.io.converters.ArrayNumberConverter;
import ucar.ma2.DataType;
import ucar.nc2.Attribute;
import ucar.nc2.CDMNode;
import ucar.nc2.Group;
import ucar.nc2.Variable;

/**
 * This is one implementation of a class that deals withretrieving data from a CDM node.
 * 
 * @author Henrique Sousa
 *
 * @param <T> Type type of node that this handles.
 */
@SuppressWarnings("deprecation")
public abstract class AbstractCDMNodeHandler<T extends CDMNode> {

    protected static final Method TO_STRING;

    static {
        try {
            TO_STRING = Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected enum InvocationType {
        ACCESSOR,
        MODIFIER;

        public static InvocationType of(Method method) {
            if (method.getName().startsWith("get")) {
                return ACCESSOR;
            } else if (method.getName().startsWith("set")) {
                return MODIFIER;
            } else {
                return null;
            }
        }

        public String getKey(Method method) {
            return method.getName().substring(3);
        }

    }

    protected final T node;
    protected final boolean readOnly;
    protected final Map<String, Object> values = new LinkedHashMap<>();
    protected final RuntimeConfiguration runtimeConfiguration;

    public AbstractCDMNodeHandler(T node, boolean readOnly, RuntimeConfiguration runtimeConfiguration) {
        if (runtimeConfiguration == null) {
            throw new IllegalArgumentException("runtimeConfiguration cannot be null");
        }
        this.node = node;
        this.readOnly = readOnly;
        this.runtimeConfiguration = runtimeConfiguration;
    }

    protected boolean isFromInterface(Object proxy, Method method) {
        if (method.isDefault()) {
            return false;
        }
        if (Modifier.isAbstract(method.getModifiers())) {
            return true;
        }
        List<Class<?>> interfaceList = Arrays.asList(proxy.getClass().getInterfaces());
        return interfaceList.contains(method.getDeclaringClass());
    }

    protected Object callActualImplementation(Object proxy, Method method, Object[] args) throws Throwable {
        /*
         * default methods already have an implementation that just needs to be invoked, but just calling
         * method.invoke(proxy, args) would recurse
         */
        try {
            // JRE 9+
            return MethodHandles.lookup()
                    .findSpecial(method.getDeclaringClass(), method.getName(),
                            MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
                            proxy.getClass())
                    .bindTo(proxy).invokeWithArguments(args);
        } catch (IllegalAccessException e) {
            // fallback to JRE 8
            Constructor<Lookup> lookupConstructor = Lookup.class.getDeclaredConstructor(Class.class);
            lookupConstructor.setAccessible(true);
            return lookupConstructor.newInstance(method.getDeclaringClass())
                    .in(method.getDeclaringClass())
                    .unreflectSpecial(method, method.getDeclaringClass())
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }
    }

    protected String getActualName(Method method, String preset) {
        if (preset.isEmpty()) {
            String name = method.getName();
            if (name.startsWith("get")) {
                name = name.substring("get".length());
            }
            return runtimeConfiguration.getRuntimeName(node, name);
        }
        return runtimeConfiguration.getRuntimeName(node, preset);
    }

    protected Object getAttribute(Method method) {
        CDLAttribute attributeAnnotation = method.getAnnotation(CDLAttribute.class);
        DataType dataType = DataType.getType(method.getReturnType(), attributeAnnotation.unsigned());
        String attName = getActualName(method, attributeAnnotation.name());
        Attribute attribute;
        if (node == null) {
            attribute = null;
        } else if (node instanceof Group)
            attribute = ((Group) node).findAttribute(attName);
        else if (node instanceof Variable)
            attribute = ((Variable) node).findAttribute(attName);
        else
            throw new IllegalStateException("I can't get an attribute from " + node.getClass());
        if (attribute == null) {
            // if a variable attribute is absent from the node, it is absent period
            String defaultValue = (node == null || node instanceof Variable) ? "" : attributeAnnotation.defaultValue();
            if (!defaultValue.isEmpty()) {
                switch (dataType) {
                case STRING:
                    return defaultValue;
                case BOOLEAN:
                    return Boolean.parseBoolean(defaultValue);
                case CHAR:
                    return defaultValue.charAt(0);
                case SHORT:
                    return Short.parseShort(defaultValue);
                case DOUBLE:
                    return Double.parseDouble(defaultValue);
                case BYTE:
                    return Byte.parseByte(defaultValue);
                case INT:
                    return Integer.parseInt(defaultValue);
                case LONG:
                    return Long.parseLong(defaultValue);
                case FLOAT:
                    return Float.parseFloat(defaultValue);
                default:
                    throw new IllegalStateException(dataType + " attribute conversion not implemented");
                }
            }
            return null;
        }
        if (method.getReturnType().isEnum()) {
            throw new UnsupportedOperationException("enum attribute not implemented yet");
        }
        if (method.getReturnType().isArray()) {
            return new ArrayNumberConverter().toJavaObject(attribute.getValues(), method.getReturnType());
        }
        if (method.getReturnType().isInterface()) {
            throw new UnsupportedOperationException("composite attribute (structure, opaque) not implemented yet");
        }
        switch (dataType) {
        case STRING:
            return attribute == null ? null : attribute.getStringValue();
        default:
            return attribute == null || attribute.getValues() == null ? null : attribute.getValues().getObject(0);
        }
    }

    protected String stringValue() {
        return values.toString() + '\n' + String.valueOf(node);
    }

}
