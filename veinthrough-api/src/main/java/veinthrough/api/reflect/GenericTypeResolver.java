package veinthrough.api.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface GenericTypeResolver<T> {
    // Method 1: by instance of a generic class
    static String GenericTypeNameOf(Object object) {
        Class<?> clazz = object.getClass();

        // Maybe: veinthrough.api.reflect.GenericTypeResolver<T>
        // Maybe: veinthrough.api.reflect.GenericTypeResolver<java.lang.String>
        ParameterizedType parameterizedType = (ParameterizedType)clazz.getGenericSuperclass();

        // Maybe: java.lang.String
        // Maybe: T
        Type type = parameterizedType.getActualTypeArguments()[0];

        // [?] will get java.lang.Class
//        Class<?> tClass = type.getClass();

        // use getTypeName() is correct
        // use getClass() is not correct
        return type.getTypeName();
    }

    static Class<?> GenericTypeOf(Object object) throws ClassNotFoundException {
        return Class.forName(GenericTypeNameOf(object));
    }

    // Method 2: by inheriting of GenericTypeResolver<T>
    default String getGenericTypeName() {
        // Maybe: veinthrough.api.reflect.GenericTypeResolver<T>
        // Maybe: veinthrough.api.reflect.GenericTypeResolver<java.lang.String>
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();

        // Maybe: java.lang.String
        // Maybe: T
        Type type = parameterizedType.getActualTypeArguments()[0];

        // [?] will get java.lang.Class
//        Class<T> tClass = (Class<T>)type.getClass();

        // use getTypeName() is correct
        // use getClass() is not correct
        return type.getTypeName();
    }

    @SuppressWarnings("unchecked")
    default Class<T> getGenericType() throws ClassNotFoundException {
        return (Class<T>) Class.forName(getGenericTypeName());
    }
}
