package veinthrough.test;


import org.junit.Test;
import veinthrough.test.env.EnvTest;
import veinthrough.test.env.SeparatorTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author veinthrough
 */
public class MultiTester {
    private List<UnitTester> unitTesters = new ArrayList<>();

    /**
     * Add a tester to the list which will be executed.
     *
     * @param unitTester tester to add
     * @return always this to provide a sequence calling
     */
    public MultiTester add(UnitTester unitTester) {
        this.unitTesters.add(unitTester);
        return this;
    }

    /**
     * Execute all testers added to the list.
     * <p>
     * Use {@link #add(UnitTester)} to add a tester to the list
     */
    @Test
    public void test() {
        MultiTester tester = new MultiTester();

        tester.add(new EnvTest());
        tester.add(new SeparatorTest());

        tester.test();
        if (!this.unitTesters.isEmpty()) {
            for (UnitTester unitTester : unitTesters) {
                System.out.println(unitTester.getClass().getSimpleName() + "----------");
                unitTester.test();
                System.out.println("-----------------------------------------------------------\n");
            }
        }
    }
}