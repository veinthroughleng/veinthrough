package veinthrough.test.io.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.io.StreamRWTest;
import veinthrough.test.io.output.PrintWriterTest;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * @see StreamRWTest
 * @see FileStreamTest
 * <p>---------------------------------------------------------
 * <pre>
 * constructors:
 * [?] writer/OuputStream一般都是不存在就创建?
 * FileWriter(String fileName)
 * FileWriter(String fileName, boolean append)
 * FileWriter(File file)
 * FileWriter(File file, boolean append)
 * FileWriter(FileDescriptor fd)
 * 不使用charset作为参数，需要charset用OutputStreamWriter来构造
 * @see StreamRWTest
 *
 * FileReader(String fileName)
 * FileReader(File file)
 * FileReader(FileDescriptor fd)
 * 不使用charset作为参数，需要charset用InputStreamReader来构造
 * @see StreamRWTest
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * APIs:
 * 完全用OutputStreamWriter/InputStreamReader来实现
 * 没有 FileReader.readLine(), BufferedReader才有
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. read/write, 没有包含charset的构造函数, PrintWriter/InputStreamReader/OutputStreamWriter才有
 * @see PrintWriterTest
 * @see StreamRWTest
 * 2. read/write, override/append
 * @see FileStreamTest#overrideTest()
 * @see FileStreamTest#appendTest()
 * </pre>
 */
@Slf4j
public class FileRWTest extends AbstractUnitTester {
    private static final String FILE_NAME = "file_RW_test.txt";
    private static final int BUF_SIZE = 100;

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void writeTest() {
        try (FileWriter out = new FileWriter(FILE_NAME)) {
            out.write("姓名: veinthrough 冷\n");
        } catch (IOException e) {
            log.info(exceptionLog(e));
        }
    }

    @Test
    public void readTest() {
        try (FileReader in = new FileReader(FILE_NAME)) {
            if (in.ready()) {
                char[] buf = new char[BUF_SIZE];
                int len = in.read(buf, 0, BUF_SIZE);
                log.info(methodLog(String.format(
                        "%d chars: %s", len, new String(buf, 0, len))));
            }
        } catch (IOException e) {
            log.info(exceptionLog(e));
        }
    }
}
