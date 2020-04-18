package veinthrough.test._class.inner;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
 * test static inner class
 * Comments:
 * 1. 在内部类不需要访问外围类对象的时候, 应该使用静态内部类)
 * 2. 不访问外围类非静态成员（通过对象），不会额外生成域
 * <p>---------------------------------------------------------
 * <pre>
 * class TalkingClock4
 * {
 *     public TalkingClock4(int);
 *
 *     static Logger access$000();
 *     public void start();
 *     public static boolean isBeep();
 *
 *     private static final Logger log;
 *     private int interval;
 *     private static final boolean beep;
 * }
 * // 不访问外围类非静态成员（通过对象），不会额外生成域
 * static class TalkingClock4$TimePrinter
 * {
 *     TalkingClock4$TimePrinter();
 *     public void actionPerformed(ActionEvent);
 * }
 * </pre>
 */

@Slf4j
public class StaticInnerClassTest extends AbstractUnitTester {
    private static final long DURATION_TEST = 2000L;
    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void staticInnerClassTest() throws InterruptedException {
        TalkingClock4 clock = new TalkingClock4(1000);
        clock.start();
        Thread.sleep(DURATION_TEST);
    }

    @Test
    public void classAnalysisTest() {
        ClassAnalyzer analyzer = new ClassAnalyzer();
        log.info(methodLog(analyzer.analyze(TalkingClock4.class)));
        log.info(methodLog(analyzer.analyze(TalkingClock4.TimePrinter.class)));
    }
}

@Slf4j
@AllArgsConstructor
class TalkingClock4 {
    private int interval;
    @Getter private static final boolean beep = false;

    public void start()
    {
        ActionListener listener = new TimePrinter();
        Timer t = new Timer(interval, listener);
        t.start();
    }

    // 内部类才可以用private/static修饰符
    // 内部类不访问外部类对象，应置static
    static class TimePrinter implements ActionListener
    {
        public void actionPerformed(ActionEvent event)
        {
            Date now = new Date();
            log.info(methodLog("now", now.toString()));
            // 访问外围类静态成员
            if (TalkingClock4.isBeep()) Toolkit.getDefaultToolkit().beep();
        }
    }
}
