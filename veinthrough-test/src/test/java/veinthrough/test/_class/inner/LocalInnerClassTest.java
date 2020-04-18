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
 * test local inner class
 *
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * class TalkingClock2
 * {
 * // 访问了beep,而beep为private，相当于beep的static访问器
 * static boolean access$0(TalkingClock2);
 * }
 *
 * class TalkingClock2$1TimePrinter
 * {
 * // 在构造函数中初始化这些自动构造的域
 * TalkingClock2$1TimePrinter(TalkingClock2, Date, boolean);
 * // 非静态内部类将会生成引用外部类对象的域，该域为final
 * final TalkingClock2 this$0;
 * // 访问的局部变量也会生成域，该域为final
 * private final Date val$now;
 * private final boolean val$beep_;
 * }
 * </pre>
 */

@Slf4j
public class LocalInnerClassTest extends AbstractUnitTester {
    private static final long DURATION_TEST = 10000L;

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void localInnerClassTest() throws InterruptedException {
        TalkingClock2 clock = new TalkingClock2(1000, true);
        clock.start(false);
        Thread.sleep(DURATION_TEST);
    }

    @Test
    public void classAnalysisTest() {
        log.info(methodLog(
                new ClassAnalyzer().analyze(TalkingClock2.class)));
    }

    public static void main(String[] args) {
        new LocalInnerClassTest().test();
    }
}

@AllArgsConstructor
@Slf4j
class TalkingClock2 {
    private int interval;
    @Setter
    private boolean beep;

    // Local variable accessed by local inner class must be final or effectively final;
    // 即接下来没有被修改，所以最好把局部内部类访问的局部变量置final
    public void start(final boolean beep_) {
        final Date now = new Date();
        // 局部内部类只能是abstract/final
        class TimePrinter implements ActionListener {
            public void actionPerformed(ActionEvent event) {
                log.info(methodLog("now", now.toString()));
                // 局部内部类可以访问外部类域
                // 局部内部类也可以访问局部变量，但必须为final
                if (beep | beep_) Toolkit.getDefaultToolkit().beep();
            }
        }

        // 修改局部内部类访问的局部变量，那么就不是effectively final
//        now = new Date(new CalendarTest().next(Calendar.MONTH, now.getTime()));

        log.info(methodLog(
                new ClassAnalyzer().analyze(TimePrinter.class)));
        ActionListener listener = new TimePrinter();
        Timer t = new Timer(interval, listener);
        t.start();
    }
}
