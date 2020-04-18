package veinthrough.api.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author veinthrough
 * <p>
 * This class give a implementation of printing a class declaration by reflect
 * Comments:
 * Generic/non-generic:
 * @see GenericClassAnalyzer#analyze(Class)
 * (1) getSuperclass/getGenericSuperclass
 * (2) getInterfaces/getGenericInterfaces
 * (3) getAnnotatedInterfaces: 注解
 * <p>---------------------------------------------------------
 * <pre>
 * APIs:
 * [Class][static]Class<?> ClassforName(String className)
 * [Class]        Class<? super T> getSuperclass()
 * [Class]        String getName()/getSimpleName()
 * [Class]        [native]int getModifiers()
 * [Class]        Constructor<?>[] getDeclaredConstructors()/
 *                Method[] getDeclaredMethods()
 *                Field[] getDeclaredFields()
 * [Constructor]        String getName()
 * [Constructor]        int getModifiers()
 * [Constructor]        Class<?>[] getParameterTypes()
 * [Method]        String getName()
 * [Method]        Class<?> getReturnType()
 * [Method]        Class<?>[] getParameterTypes()
 * [Method]        int getModifiers()
 * [Field]        String getName()
 * [Field]        Class<?> getType()
 * [Field]        int getModifiers()
 * [Modifier][static]String toString(int mod)
 * </pre>
 */
public class ClassAnalyzer{
    private static final String SPACE = " ";
    private static final String FOUR_SPACE = "    ";

    public String analyze(String className) {
        String result = "";
        try {
            // print class name and superclass name (if != Object)
            Class<?> clazz = Class.forName(className);
            result = analyze(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String analyze(Class<?> clazz) {
        StringBuilder result = new StringBuilder();

        // modifiers
        result.append(modifiersString(clazz.getModifiers()));

        // name
        result.append("class ").append(clazz.getName());

        // super
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            result.append(" extends ").append(superClass.getSimpleName());
        }

        result.append("\n{\n");
        result.append(analyzeConstructors(clazz));
        result.append("\n");
        result.append(analyzeMethods(clazz));
        result.append("\n");
        result.append(analyzeFields(clazz));
        result.append("}\n");
        return result.toString();
    }

    private String analyzeConstructors(Class<?> clazz) {
        StringBuilder result = new StringBuilder();
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            // modifiers
            result.append(FOUR_SPACE)
                    .append(modifiersString(constructor.getModifiers()));

            // name
            result.append(constructor.getName()).append("(");

            // parameter types
            Class<?>[] paramTypes = constructor.getParameterTypes();
            int numParams = paramTypes.length;
            for (int j = 0; j < numParams; j++) {
                if (j > 0) result.append(", ");
                result.append(paramTypes[j].getSimpleName());
            }
            result.append(");\n");
        }
        return result.toString();
    }

    private String analyzeMethods(Class<?> clazz) {
        StringBuilder result = new StringBuilder();
        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            Class<?> retType = method.getReturnType();

            // modifiers
            result.append(FOUR_SPACE)
                    .append(modifiersString(method.getModifiers()));

            // return type
            result.append(retType.getSimpleName()).append(SPACE);

            // name
            result.append(method.getName()).append("(");

            // parameter types
            Class<?>[] paramTypes = method.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if (j > 0) result.append(", ");
                result.append(paramTypes[j].getSimpleName());
            }
            result.append(");\n");
        }
        return result.toString();
    }

    private String analyzeFields(Class<?> clazz) {
        StringBuilder result = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            Class<?> type = field.getType();

            // modifiers
            result.append(FOUR_SPACE)
                    .append(modifiersString(field.getModifiers()));

            // type
            result.append(type.getSimpleName()).append(SPACE);

            // name
            result.append(field.getName()).append(";\n");
        }
        return result.toString();
    }

    private String modifiersString(int modifier) {
        String modifiers = Modifier.toString(modifier);
        if (modifiers.length() > 0)
            return modifiers + SPACE;
        else
            return "";
    }
}
