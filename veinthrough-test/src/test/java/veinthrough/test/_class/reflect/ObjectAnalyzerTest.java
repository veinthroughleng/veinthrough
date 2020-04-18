package veinthrough.test._class.reflect;

import org.junit.Test;
import veinthrough.api.reflect.ObjectAnalyzer;
import veinthrough.test.AbstractUnitTester;

import java.util.ArrayList;

public class ObjectAnalyzerTest extends AbstractUnitTester {
    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {

    }

    @Test
    public void objectAnalyzerTest() {
        ArrayList<Integer> squares = new ArrayList<>();
        for(int i=1; i<5; i++) {
            squares.add(i*i);
        }
        System.out.println(new ObjectAnalyzer().analyze(squares));
    }
}
