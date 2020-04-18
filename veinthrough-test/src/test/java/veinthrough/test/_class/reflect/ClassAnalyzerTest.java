package veinthrough.test._class.reflect;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.generic.Either;
import veinthrough.api.reflect.ClassAnalyzer;
import veinthrough.api.reflect.GenericClassAnalyzer;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test._class.Employee;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * This class give a implementation of printing a class declaration by reflect
 * Comments:
 * Generic/non-generic:
 * @see ClassAnalyzer#analyze(Class): non-generic
 * @see GenericClassAnalyzer#analyze(Class): generic
 * (1) getSuperclass/getGenericSuperclass
 * (2) getInterfaces/getGenericInterfaces
 * (3) getAnnotatedInterfaces: 注解
 */

@Slf4j
public class ClassAnalyzerTest extends AbstractUnitTester {
    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    /**
     * non-generic
     */
    @Test
    public void classAnalyzerTest() {
        log.info(methodLog(
                new ClassAnalyzer().analyze(Employee.class)));
    }

    /**
     * generic
     */
    @Test
    public void genericClassAnalyzerTest() {
        log.info(methodLog(
                new GenericClassAnalyzer().analyze(Either.class)));
    }
}
