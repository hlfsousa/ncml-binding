package hsousa.ncml.io.read;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

import hsousa.ncml.annotation.CDLAttribute;
import hsousa.ncml.annotation.CDLGroup;
import hsousa.ncml.annotation.CDLVariable;
import ucar.ma2.Array;
import ucar.nc2.CDMNode;
import ucar.nc2.Group;
import ucar.nc2.Variable;

public class GroupHandler extends AbstractCDMNodeHandler<Group> implements InvocationHandler {

    private final Map<Method, Object> invocationCache = new HashMap<>();

    public GroupHandler(Group group, boolean readOnly) {
        super(group, readOnly);
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
            ParameterizedType valueType = (ParameterizedType) ((ParameterizedType) method.getGenericReturnType())
                    .getActualTypeArguments()[1];
            Class<?>[] interfaces = new Class<?>[] { (Class<?>)valueType.getRawType() };
            return getMapped(node.getVariables(), varName, variable -> Proxy.newProxyInstance(classLoader,
                    interfaces, new VariableHandler(variable, valueType, readOnly)));
        }
        Variable variable = node == null ? null : node.findVariable(varName);
        if (readOnly && variable == null) {
            return null;
        }
        if (Array.class.isAssignableFrom(method.getReturnType())) {
            return variable == null ? null : variable.read();
        }
        return Proxy.newProxyInstance(classLoader, new Class<?>[] { method.getReturnType() },
                new VariableHandler(variable, method.getGenericReturnType(), readOnly));
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
                    interfaces, new GroupHandler(child, readOnly)));
        }
        Group child = node == null ? null : node.findGroup(groupName);
        if (readOnly && child == null) {
            return null;
        }
        return Proxy.newProxyInstance(classLoader, new Class<?>[] { method.getReturnType() },
                new GroupHandler(child, readOnly));
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