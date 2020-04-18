package veinthrough.test._class.inner;

import lombok.AllArgsConstructor;
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
 * test general inner class
 * Comments:
 * 1. 在内部类不需要访问外围类对象的时候, 应该使用静态内部类
 * <p>---------------------------------------------------------
 * <pre>
 * class TalkingClock
 * {
 * // 访问了beep,而beep为private，相当于beep的static访问器
 * static boolean access$0(TalkingClock);
 * }
 * class TalkingClock$TimePrinter
 * {
 * // 在构造函数中初始化这些自动构造的域
 * TalkingClock$TimePrinter(TalkingClock);
 * // 非静态内部类将会生成引用外部类对象的域，该域为final
 * final TalkingClock this$0;
 * }
 * </pre>
 */

@Slf4j
public class InnerClassTest extends AbstractUnitTester {
    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    private static final long DURATION_TEST = 10000L;

    @Override
    public void test() {
    }

    @Test
    public void innerClassTest() throws InterruptedException {
        TalkingClock clock = new TalkingClock(1000, true);
        clock.start();
        Thread.sleep(DURATION_TEST);
    }

    @Test
    public void classAnalysisTest() {
        ClassAnalyzer analyzer = new ClassAnalyzer();
        log.info(methodLog(analyzer.analyze(TalkingClock.class.getName())));
        log.info(methodLog(analyzer.analyze(TalkingClock.TimePrinter.class)));
    }

    public static void main(String[] args) {
        new InnerClassTest().test();
    }
}

@AllArgsConstructor
@Slf4j
class TalkingClock {
    private int interval;
    private boolean beep;

    public void start() {
        ActionListener listener = new TimePrinter();
        Timer t = new Timer(interval, listener);
        t.start();
    }

    // 内部类才可以用private/static修饰符
    class TimePrinter implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            Date now = new Date();
            log.info(methodLog("now", now.toString()));
            // 访问外围类成员
            if (beep) Toolkit.getDefaultToolkit().beep();
        }
    }
}
