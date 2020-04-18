package veinthrough.test._class.inner;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.reflect.ClassAnalyzer;
import veinthrough.test.AbstractUnitTester;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>
 * test anonymous inner class
 * Comments:
 * 1. 匿名类不能有构造函数, 所以不能有任何构造参数
 * <p>---------------------------------------------------------
 * <pre>
 * class TalkingClock3
 * {
 * // 访问了beep,而beep为private，相当于beep的static访问器
 * static boolean access$0(TalkingClock3);
 * }
 *
 * class TalkingClock3$1TimePrinter
 * {
 * // 在构造函数中初始化这些自动构造的域
 * TalkingClock3$1TimePrinter(TalkingClock3, Date, boolean);
 * // 非静态内部类将会生成引用外部类对象的域，该域为final
 * final TalkingClock3 this$0;
 * // 访问的局部变量也会生成域，该域为final
 * private final Date val$now;
 * private final boolean val$beep_;
 * }
 * </pre>
 */

@Slf4j
public class AnonymousInnerClassTest extends AbstractUnitTester {
    private static final long DURATION_TEST = 2000L;

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void anonymousInnerClassTest() throws InterruptedException {
        TalkingClock3 clock = new TalkingClock3(1000, false);
        clock.start(true);

        Thread.sleep(DURATION_TEST);
    }

    @Test
    public void classAnalysisTest() {
        log.info(methodLog(
                new ClassAnalyzer().analyze(TalkingClock3.class)));
    }

    public static void main(String[] args) {
        new AnonymousInnerClassTest().test();
    }
}

@AllArgsConstructor
@Slf4j
class TalkingClock3 {
    private int interval;
    @Setter
    private boolean beep;

    // Local variable accessed by local inner class must be final or effectively final;
    // 即接下来没有被修改，所以最好把局部内部类访问的局部变量置final
    public void start(final boolean beep_) {
        final Date now = new Date();
        Timer t = new Timer(interval, new ActionListener() {
            // 局部内部类只能是abstract/final
            public void actionPerformed(ActionEvent event) {
                // 只能通过this.getClass()来获取class
                log.info(methodLog(
                        new ClassAnalyzer().analyze(this.getClass())));
                log.info(methodLog("now", now.toString()));
                // 局部内部类可以访问外部类域
                // 局部内部类也可以访问局部变量，但必须为final
                if (beep | beep_) Toolkit.getDefaultToolkit().beep();
            }
        });

        // 修改局部内部类访问的局部变量，那么就不是effectively final
//        now = new Date(new CalendarTest().next(Calendar.MONTH, now.getTime()));
        t.start();
    }
}
