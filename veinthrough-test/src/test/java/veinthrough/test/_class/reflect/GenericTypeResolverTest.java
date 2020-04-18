package veinthrough.test._class.reflect;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.reflect.GenericTypeResolver;

import static veinthrough.api.util.MethodLog.*;

@Slf4j
public class GenericTypeResolverTest {
    private class GenericClass<T> implements GenericTypeResolver<T>{
    }

    @Test
    // Method 1: by instance of a generic class
    public void genericTypeResolverTest1() throws ClassNotFoundException {
        // [?] 必须带上{}, 类型参数才会实例化, 否则会得到T而不是java.lang.String
        GenericClass<String> genericInstance = new GenericClass<String>(){};
//        GenericClass<String> genericInstance = new GenericClass<String>();
        log.info(methodLog(
                        GenericTypeResolver.GenericTypeNameOf(genericInstance)));
        log.info(methodLog(
                GenericTypeResolver.GenericTypeOf(genericInstance).getName()));
    }

    @Test
    // Method 2: by inheriting of GenericTypeResolver<T>
    public void genericTypeResolverTest2() throws ClassNotFoundException {
        // [?] 必须带上{}, 类型参数才会实例化, 否则会得到T而不是java.lang.String
        GenericClass<String> genericInstance = new GenericClass<String>(){};
//        GenericClass<String> genericInstance = new GenericClass<String>();
        log.info(methodLog(
                        genericInstance.getGenericTypeName()));
        log.info(methodLog(
                genericInstance.getGenericType().getName()));
    }
}
