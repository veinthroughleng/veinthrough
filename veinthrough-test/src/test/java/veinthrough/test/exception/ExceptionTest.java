package veinthrough.test.exception;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * 1. Exceptions architecture.
 * (1) Throwable-+---Error
 *               |
 *               +---Exception-+---IOException
 *                             |
 *                             +---RuntimeException
 * (2) unchecked-+---Error
 *               |
 *               +---RuntimeException
 * (3) checked-------IOException
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * 1. 一个方法必须声明所有可能抛出的checked exception，
 * 而unchecked exception要么不可控制（Error），要么就应该避免发生（逻辑问题）。
 * 2. 再次抛出异常与异常链：在catch子句中可以抛出一个异常：
 * (1) 改变异常类型/抛出更详细的异常
 * (2) checked exception --> unchecked exception
 * 3. finnaly中也有可能抛出异常
 * (1) 使用2个try: 原来的异常被抑制, close()抛出的异常将会被抛出
 * (2) 使用try(resource): 原来的异常重新抛出, close()抛出的exception将会被抑制
 * </pre>
 */
@Slf4j
public class ExceptionTest extends AbstractUnitTester {
    // make it non-existed
    private static final String fileName = "exception_test.txt";

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    // finally(调用close())中也有可能抛出异常
    // 3.(1) 使用2个try
    @Test
    public void doubleTryTest() {
        InputStream in = null;
        try {
            try {
                log.info(methodLog(1, "New file stream from " + fileName));
                in = new FileInputStream(fileName);

                log.info(methodLog(2, "Read:" + in.read()));
            } catch (FileNotFoundException e) {
                log.info(methodLog(3, "Exception:" + e.getMessage()));
            } catch (IOException e) {
                log.info(methodLog(4, "Exception:" + e.getMessage()));
            } finally {
                log.info(methodLog(5, "In finally to close file stream"));
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            log.info(methodLog(6, "Exception:" + e.getMessage()));
        }
    }

    // 3.(2) 使用try(resource)
    @Test
    public void tryResourceTest() {
        log.info(methodLog(1, "New file stream from " + fileName));
        try (InputStream in = new FileInputStream(fileName)) {
            log.info(methodLog(2, "Read:" + in.read()));
        } catch (IOException e) {
            log.info(methodLog(3, "Exception:" + e.getMessage()));
        }
    }
}
