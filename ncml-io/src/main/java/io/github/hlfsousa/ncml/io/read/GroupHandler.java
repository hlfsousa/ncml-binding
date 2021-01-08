package io.github.hlfsousa.ncml.io.read;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

import io.github.hlfsousa.ncml.annotation.CDLAttribute;
import io.github.hlfsousa.ncml.annotation.CDLGroup;
import io.github.hlfsousa.ncml.annotation.CDLVariable;
import io.github.hlfsousa.ncml.io.AttributeConventions;
import io.github.hlfsousa.ncml.io.ConvertUtils;
import ucar.ma2.Array;
import ucar.nc2.CDMNode;
import ucar.nc2.Group;
import ucar.nc2.Variable;

public class GroupHandler extends AbstractCDMNodeHandler<Group> implements InvocationHandler {

    private final Map<Method, Object> invocationCache = new HashMap<>();
    private final AttributeConventions attributeConventions;
    private final ConvertUtils convertUtils;

    public GroupHandler(Group group, boolean readOnly, Map<String, String> runtimeProperties) {
        super(group, readOnly, runtimeProperties);
        attributeConventions = new AttributeConventions();
        convertUtils = ConvertUtils.getInstance();
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
            values.put(key, args[0]);
            return null;
        default:
            throw new IllegalStateException("Unsupported method type " + methodType + " -- " + method);
        }

        Object result;
        if (method.isAnnotationPresent(CDLGroup.class)) {
            result = getGroup(method);
        } else if (method.isAnnotationPresent(CDLAttribute.class)) {
            result = getAttribute(method);
        } else if (method.isAnnotationPresent(CDLVariable.class)) {
            result = getVariable(method);
        } else {
            throw new UnsupportedOperationException("Not yet implemented: " + method);
        }
        invocationCache.put(method, result);
        return result;
    }

    protected Object getVariable(Method method) throws Throwable {
        CDLVariable variableAnnotation = method.getAnnotation(CDLVariable.class);
        String varName = getActualName(method, variableAnnotation.name());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (isMapped(varName)) {
            Type valueType = ((ParameterizedType) method.getGenericReturnType())
                    .getActualTypeArguments()[1];
            if (valueType instanceof ParameterizedType) {
                // this is a Variable
                ParameterizedType variableType = (ParameterizedType) valueType;
                Class<?>[] interfaces = new Class<?>[] { (Class<?>) variableType.getRawType() };
                return getMapped(node.getVariables(), varName, variable -> Proxy.newProxyInstance(classLoader,
                        interfaces, new VariableHandler(variable, variableType, readOnly, runtimeProperties)));
            } else {
                // scalar value
                return getMapped(node.getVariables(), varName, variable -> {
                    try {
                        return variable == null ? null
                                : convertUtils.toJavaObject(variable.read(), (Class<?>) valueType);
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to read variable " + variable.getFullName());
                    }
                });
            }
        }
        Variable variable = node == null ? null : node.findVariable(varName);
        if (readOnly && variable == null) {
            return null;
        }
        if (Array.class.isAssignableFrom(method.getReturnType())) {
            /*
             * this is no longer used for generated classes, but I'm keeping for backwards
             * compatibility and in case someone writes an interface manually that returns
             * Array instead of an interface
             */
            return variable == null ? null : attributeConventions.readNumericArray(variable);
        } else if (method.getReturnType().isInterface()) {
            return Proxy.newProxyInstance(classLoader, new Class<?>[] { method.getReturnType() },
                    new VariableHandler(variable, method.getGenericReturnType(), readOnly, runtimeProperties));
        } else {
            return convertUtils.toJavaObject(
                    variable == null ? null : attributeConventions.readNumericArray(variable), method.getReturnType());
        }
    }

    private Object getGroup(Method method) {
        CDLGroup groupAnnotation = method.getAnnotation(CDLGroup.class);
        String groupName = getActualName(method, groupAnnotation.name());
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (isMapped(groupName)) {
            Class<?> valueType = (Class<?>) ((ParameterizedType) method.getGenericReturnType())
                    .getActualTypeArguments()[1];
            Class<?>[] interfaces = new Class<?>[] { valueType };
            return getMapped(node.getGroups(), groupName, child -> Proxy.newProxyInstance(classLoader,
                    interfaces, new GroupHandler(child, readOnly, runtimeProperties)));
        }
        Group child = node == null ? null : node.findGroup(groupName);
        if (readOnly && child == null) {
            return null;
        }
        return Proxy.newProxyInstance(classLoader, new Class<?>[] { method.getReturnType() },
                new GroupHandler(child, readOnly, runtimeProperties));
    }

    private <N extends CDMNode> Map<String, Object> getMapped(Iterable<N> children, String groupName, Function<N, Object> factory) {
        Map<String,Object> map = new LinkedHashMap<>();
        Pattern regex = Pattern.compile(groupName.substring(groupName.indexOf(':') + 1));
        for (N child : children) {
            Matcher matcher = regex.matcher(child.getShortName());
            if (matcher.matches()) {
                map.put(child.getShortName(), factory.apply(child));
            }
        }
        if (readOnly) {
            map = Collections.unmodifiableMap(map);
        }
        return map;
    }

    private boolean isMapped(String propertyName) {
        return propertyName.matches("[a-zA-Z_][a-zA-Z_0-9]*:.*");
    }

}
