package veinthrough.test.io.file;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.io.*;

import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * FileOutputStream(File file)
 * FileOutputStream(File file, boolean append)
 * FileOutputStream(String path)
 * FileOutputStream(String path, boolean append)
 * FileOutputStream(FileDescriptor fd)
 * FileInputStream(String name)
 * FileInputStream(File file)
 * FileInputStream(FileDescriptor fd)
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. write in override mode and read
 * 2. write in append mode and read
 * 3. file descriptor test
 * @see FileDescriptorTest#overrideFdTest()
 * @see FileDescriptorTest#appendFdTest()
 * </pre>
 */
@Slf4j
public class FileStreamTest extends AbstractUnitTester {
    private static final int READ_LENGTH = 100;
    private static final String FILE_NAME = "file_stream_test.txt";
    private static final boolean OVERRIDE = false, APPEND = true;

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void overrideTest() {
        _test(OVERRIDE);
    }

    @Test
    public void appendTest() {
        _test(APPEND);
    }

    @Test
    public void overrideFdTest() throws IOException {
        new FileDescriptorTest().overrideFdTest();
    }

    @Test
    public void appendFdTest() throws IOException {
        new FileDescriptorTest().appendFdTest();
    }

    private void _test(boolean mode) {
        try (FileOutputStream fos1 = new FileOutputStream(FILE_NAME)) {
            fos1.write('A');
        } catch (IOException e) {
            log.warn(exceptionLog(e));
        }

        try (FileOutputStream fos2 = new FileOutputStream(FILE_NAME, mode)) {
            fos2.write('a');
        } catch (IOException e) {
            log.warn(exceptionLog(e));
        }

        try (FileInputStream fis = new FileInputStream(FILE_NAME)) {
            byte[] buf = new byte[READ_LENGTH];
            int len = fis.read(buf, 0, READ_LENGTH);
            log.info(methodLog(
                    String.format("%4d bytes:%s", len, new String(buf).substring(0, len))));
        } catch (IOException e) {
            log.warn(exceptionLog(e));
        }
    }
}
