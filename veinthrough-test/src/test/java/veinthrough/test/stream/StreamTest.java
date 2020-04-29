package veinthrough.test.stream;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.collection.CollectionToMap;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.async.ForkJoinTest;
import veinthrough.test.collection.TraverseTest;
import veinthrough.test.guava.RangeTest;
import veinthrough.test.string.CheckSumTest;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 *
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. list to map
 * @see CollectionToMap
 * 2. Collection <--> Stream<T> <--> IntStream
 * 特殊的Stream会有额外的功能(比如IntStream的sum)
 * @see ForkJoinTest.WordMatchCounter#compute(), Stream<T> -> IntStream
 * @see TraverseTest#arrayListTest(), IntStream -> Stream<IntTeger> -> List<T>
 * @see TraverseTest#vectorTest(), IntStream -> Stream<IntTeger> -> Vector<T>
 *
 * 3. range of integer
 * (1) IntStream range(int startInclusive, int endExclusive)
 * (2) Stream.iterate(initial value, next value),
 *    Java 9: Stream.iterate(initial value, Predicate, next value), supports a predicate (condition),
 *    and the stream.iterate will stop if the predicate is false.
 * (3) Guava's Range havn't stream operations.
 * @see RangeTest
 * 4. String -> Stream<char>/IntStream
 * (1) 使用Stream.of(T...)不行, 用string.toCharArray()返回的char[]作为参数, 不会当作数组处理而是整个当作Object处理,
 *   因为char[]不会自动转换成Character[]
 * (2) String.chars: String -> IntStream
 * @see veinthrough.test.string.CheckSumTest#checkSum(String)
 * 5. 使用Guava的source and sink, 直接读出来是List<String>或者String更好的支持Stream操作
 * @see CheckSumTest#checkSumByLine()
 * @see CheckSumTest#checkSumByChar()
 * </pre>
 */
@Slf4j
public class StreamTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    @Test
    public void intStreamTest() {
    }

    // 2.(1) IntStream range(int startInclusive, int endExclusive)
    @Test
    public void integerRangeTest1() {
        final int numPerLine = 10;
        List<String> charsets =
                Lists.newArrayList(Charset.availableCharsets().keySet());
        IntStream.range(0, charsets.size())
                .forEach(index -> {
                    if (index == charsets.size() - 1) System.out.println(charsets.get(index));
                    else if (index % numPerLine == 0) System.out.println(charsets.get(index) + ",");
                    else System.out.print(charsets.get(index) + ",");
                });
    }

    // get stream of wrapper objects which can be collected by Collectors methods
    @Test
    public void integerRangeTest2() {
        log.info(methodLog(
                IntStream.range(1, 50)
                        // IntStream -> Stream<Integer>才能使用collect
                        .boxed()
                        .collect(Collectors.toList())
                        .toString()));
    }

    // 2.(2) Stream.iterate(initial value, next value)
    @Test
    public void integerRangeTest3() {
        log.info(methodLog(
                // 0 ... 9
                "10 numbers", Stream.iterate(0, n -> n + 1)
                        .limit(10)
                        .collect(Collectors.toList())
                        .toString(),
                // Java 9: Stream.iterate(initial value, Predicate, next value), supports a predicate (condition),
                // and the stream.iterate will stop if the predicate is false.
//                Stream.iterate(0, n -> n < 10 , n -> n + 1)

                // odds
                "10 odds", Stream.iterate(0, n -> n + 1)
                        .filter(x -> x % 2 != 0)
                        .limit(10)
                        .collect(Collectors.toList()).toString(),

                // Fibonacci
                "Fibonacci", Stream.iterate(new int[]{0, 1}, n -> new int[]{n[1], n[0] + n[1]})
                        .limit(10)
                        .map(n -> n[0])
                        .collect(Collectors.toList())
                        .toString()));
    }

    /**
     * 4. String -> Stream<char>/IntStream
     * (1) 使用Stream.of(T...)不行, 用string.toCharArray()返回的char[]作为参数, 不会当作数组处理而是整个当作Object处理,
     * 因为char[]不会自动转换成Character[]
     * (2) String.chars: String -> IntStream
     *
     * @see veinthrough.test.string.CheckSumTest#checkSum(String)
     */
    @Test
    public void stringStreamTest() {
    }

    /**
     * 5. 多使用Guava的source and sink, 直接读出来是List<String>或者String更好的支持Stream操作
     *
     * @see CheckSumTest#checkSumByLine()
     * @see CheckSumTest#checkSumByChar()
     */
    @Test
    public void fileStreamTest() {
    }
}
