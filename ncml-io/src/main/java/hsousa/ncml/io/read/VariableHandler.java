package hsousa.ncml.io.read;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import hsousa.ncml.annotation.CDLAttribute;
import ucar.ma2.Array;
import ucar.nc2.Variable;

public class VariableHandler extends AbstractCDMNodeHandler<Variable> implements InvocationHandler {

    private final Map<Method, Object> invocationCache = new HashMap<>();
    private Type valueType;

    public VariableHandler(Variable variable, Type valueType, boolean readOnly) {
        super(variable, readOnly);
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
        } else if (method.getDeclaringClass().equals(hsousa.ncml.declaration.Variable.class)) {
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
        Array arrayValue = node.read();
        Class<?> expectedType;
        if (valueType instanceof ParameterizedType) {
            expectedType = (Class<?>) ((ParameterizedType) valueType).getActualTypeArguments()[0];
        } else {
            expectedType = (Class<?>) valueType;
        }
        if (expectedType.isAssignableFrom(Array.class)) {
            return arrayValue;
        }
        if (!node.isScalar()) {
            // TODO converter registration
            throw new IllegalStateException("I can't convert Array to something else yet");
        }
        return arrayValue.getObject(0);
    }

}
