package veinthrough.test._class;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.c_plus_plus.ParamTest;

import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * @see DerivedTest
 * @see Employee
 * @see ParamTest
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. [Employee]私有数据域+公有域访问器+公有域更改器，这里用lombok实现.
 * 2. [Employee]final 域，类似于指针常量，没有@Setter:
 * 3. [ParamTest]Java中的参数传递都为值传递:
 * 4. [Employee]构造函数：如果定义了有参构造函数，也应该定义无参构造函数
 * new/未初始化对象都会默认调用无参构造函数
 * 子类自动调用
 * 有些框架需要: @entity in Spring Data Rest, Json网络传输, Serializable
 * </pre>
 */
@Slf4j
public class ClassTest extends AbstractUnitTester {

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    /**
     * SuperClass.isAssignableFrom(InheritedClass)
     * @see Employee#equals(Object), instance of
     * @see Employee#equals2(Object), getClass() != otherObject.getClass()
     */
    @Test
    public void assignableClassTest() {
        log.info(methodLog(
                "Employee is assignable from manager", "" + Employee.class.isAssignableFrom(Manager.class),
                "Manager is assignable from employee", "" + Manager.class.isAssignableFrom(Employee.class)));
    }
}
