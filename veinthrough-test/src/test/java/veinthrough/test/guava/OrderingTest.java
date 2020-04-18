package veinthrough.test.guava;

import veinthrough.test.AbstractUnitTester;
import veinthrough.test.generic.GenericTest;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * APIs:
 * @see  veinthrough.api.generic.PairAlg#minMax(Comparable[])
 * @see  veinthrough.api.generic.PairAlg#minMax(Iterable)
 * @see  veinthrough.api.generic.PairAlg#minMax2(Comparable[])
 * @see  veinthrough.api.generic.PairAlg#minMax2(Iterable)
 * @see  GenericTest#wildcardSuperTest()
 *
 * 生成ordering:
 *   1. [static]get Ordering<T> from Comparator<T>
 *   2. implement the abstract Ordering<T> by override compare()
 *   3. [static] natural(), 使用既有的compareTo() from Comparable<T>
 *   4. Comparator.naturalOrder(), 使用既有的compareTo() from Comparable<T>作为comparator
 *   5. [static] usingToString(), 先使用toString()，再根据String排序
 *   6. onResultOf(): 对集合中元素调用 Function, 再按返回值用当前排序器排序
 *   7. reverse()
 *   8. nullsFirst()/nullsLast()
 *   9. fluentIterable:链式操作, 链式调用产生的排序器时, 应该从后往前读;
 *   是因为每次链式调用都是用后面的方法包装了前面的排序
 *
 * 使用ordering计算:
 *   10. min()/max(): 使用生成的ordering来计算min/max
 * </pre>
 */
@SuppressWarnings("unused")
public class OrderingTest extends AbstractUnitTester {
    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }
}
