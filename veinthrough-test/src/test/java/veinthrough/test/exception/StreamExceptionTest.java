package veinthrough.test.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.async.BlockingQueueTest;
import veinthrough.test.async.FutureTest;
import veinthrough.test.async.ThreadPoolTest;

/**
 * @author veinthrough
 * <p>
 * Handle exception in Stream:
 * 1. CheckedException -> RuntimeException, 将终止程序
 * @see BlockingQueueTest#wordMatchCountTest()
 * 2. CheckedException -> Either.left, 将忽略Exception并计算最终结果
 * @see ThreadPoolTest.WordMatchCounter#call()
 * 3. CheckedException/task -> Either.left, 将打印Exception并计算最终结果
 * @see FutureTest.WordMatchCounter#call()
 * </pre>
 */
@Slf4j
public class StreamExceptionTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    /**
     * @see BlockingQueueTest#wordMatchCountTest()
     */
    @Test
    public void toRuntimeExceptionTest() {
    }

    /**
     * @see ThreadPoolTest.WordMatchCounter#call()
     */
    @Test
    public void ignoreExceptionTest() {
    }

    /**
     * @see FutureTest.WordMatchCounter#call()
     */
    @Test
    public void logThenContinueTest() {
    }
}
