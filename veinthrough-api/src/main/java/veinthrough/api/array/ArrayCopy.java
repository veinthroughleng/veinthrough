package veinthrough.api.array;

import java.lang.reflect.Array;

/**
 * @author Veinthrough
 */
public class ArrayCopy {
    /**
     * This method attempts to grow an source by allocating source new source and copying all elements.
     *
     * @param source    the array to grow
     * @param newLength the new length
     * @return larger array that contains all elements of source. However, the returned source has
     * type Object[], not the same type as source
     */
    // 1. 不能接受原始(primitive)类型, 如int[]
    // 2. 返回的类型Object[] can't be converted to other array, like String[]
    // 从定义开始的Object[]不能转化成其他数组
    // 除非是object[]作为中间状态, 从其他数组转换成object[]再转换成其他数组
    // 如String[] -> Object[] -> String[]
    // 因为Java数组会记住每个元素的类型
    @Deprecated
    public static Object[] badCopyOf(Object[] source, int newLength)
    {
        Object[] newArray = new Object[newLength];
        System.arraycopy(source, 0, newArray, 0, Math.min(source.length, newLength));
        return newArray;
    }

    /**
     * This method grows an array by allocating source new array of the same type and
     * copying all elements.
     *
     * @param source the array to grow. This can be an object array or source primitive
     *               type array
     * @return larger array that contains all elements of source.
     */
    // 1. 参数和返回类型都为Object而非Object[]
    // 2. use reflect
    // [?] @SuppressWarnings
    @SuppressWarnings("SuspiciousSystemArraycopy")
    public static Object copyOf(Object source, int newLength) {
        Class cl = source.getClass();
        if (!cl.isArray()) return null;
        Class componentType = cl.getComponentType();
        int length = Array.getLength(source);
        // new an array by reflect
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(source, 0, newArray, 0, Math.min(length, newLength));
        return newArray;
    }
}
