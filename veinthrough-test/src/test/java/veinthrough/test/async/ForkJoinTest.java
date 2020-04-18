package veinthrough.test.async;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.exception.StreamExceptionTest;

import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * This program demonstrates the fork-join framework.
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Calculate the rate of double numbers larger than 0.5 in 0...1 by conduct a 1000000 random test.
 * 2. Find all keyword matches in files of a directory:
 *   (1) by recursively submitting tasks
 *   提交任务: [递归]每个子目录重新通过thread pool提交任务,
 *   收集数据: [递归]累加Future.get()获取结果
 *   线程数量: 和directory数量相关
 *   @see ThreadPoolTest#wordMatchCountTest()
 *   (2) by recursively invoke FutureTasks
 *   提交任务: [递归]每个子目录重新通过new Thread提交任务,
 *   收集数据: [递归]累加Future.get()获取结果
 *   线程数量: 和directory数量相关
 *   @see FutureTest#wordMatchCountTest()
 *   (3) by fork-join framework
 *   提交任务: [递归]每个子目录重新通过ForkJoinTask.invokeAll提交任务,
 *   收集数据: [递归]累加task.join获取结果
 *   线程数量: 和directory数量相关
 *   @see ForkJoinTest#wordMatchCountTest()
 *   (4) [非递归] by Multi-threads co-work through BlockingQueue
 *   提交任务: [非递归]1个file enumeration task, 固定数量的search task
 *   收集数据: [非递归]累加search task's Future.get()获取结果
 *   线程数量: 固定数量, 更好的控制
 *   @see BlockingQueueTest#wordMatchCountTest()
 * 3. Handle exception in Stream:
 * @see StreamExceptionTest
 * </pre>
 */
@Slf4j
public class ForkJoinTest extends AbstractUnitTester {
    private static final String directory =
            "D:\\Cloud\\Projects\\IdeaProjects\\test\\veinthrough\\test\\src\\main\\java\\veinthrough\\test";
    private static final String keyword = "AbstractUnitTester";
    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void NumberCountTest() {
        final int SIZE = 1000000;
        double[] numbers = new double[SIZE];
        for (int i = 0; i < SIZE; i++) numbers[i] = Math.random();

        NumberCounter counter =
                new NumberCounter(numbers, 0, numbers.length,
                        number -> number > 0.5);
        new ForkJoinPool().invoke(counter);
        log.info(methodLog(
                "Total(0<=n<1)", "" + SIZE,
                "Counts of >0.5", "" + counter.join(),
                "Rate of >0.5", "" + (double) counter.join() / numbers.length));
    }

    @Test
    public void wordMatchCountTest() {
        WordMatchCounter counter =
                new WordMatchCounter(0, new File(directory));
        new ForkJoinPool().invoke(counter);
        log.info(methodLog(
                "Matches", String.valueOf(counter.join())));
    }

    @FunctionalInterface
    interface Filter {
        boolean accept(double t);
    }

    @AllArgsConstructor
    class NumberCounter extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 1000;
        private double[] values;
        private int from;
        private int to;
        private Filter filter;

        @Override
        protected Integer compute() {
            if (to - from < THRESHOLD) {
                int count = 0;
                for (int i = from; i < to; i++) {
                    if (filter.accept(values[i])) count++;
                }
                return count;
            } else {
                int mid = (from + to) / 2;
                NumberCounter first = new NumberCounter(values, from, mid, filter);
                NumberCounter second = new NumberCounter(values, mid, to, filter);
                invokeAll(first, second);
                return first.join() + second.join();
            }
        }
    }

    @SuppressWarnings("Duplicates")
    @AllArgsConstructor
    public static class WordMatchCounter extends RecursiveTask<Integer> {
        private static final boolean ALL_FILES = true;
        private static final AtomicLong threadNum = new AtomicLong(1);
        @Getter
        private long id;
        private List<File> files;
        private boolean allFiles;

        static long generateId() {
            return threadNum.getAndIncrement();
        }

        WordMatchCounter(long id, List<File> files) {
            this(id, files, false);
        }

        WordMatchCounter(long id, File directory) {
            // noinspection ConstantConditions
            this(id, ImmutableList.copyOf(directory.listFiles()));
        }

        @Override
        public Integer compute() {
            int counter = 0;
            if (files != null && files.size() != 0) {
                if (allFiles) {
                    counter = files.stream()
                            .mapToInt(this::search)
                            .sum();
                } else {
                    List<RecursiveTask<Integer>> tasks = new ArrayList<>();
                    List<File> filesOfDir = new ArrayList<>();
                    for (File file : files) {
                        if (!file.isDirectory()) {
                            filesOfDir.add(file);
                        } else {
                            // 添加任务, 每个子目录作为一个任务
                            tasks.add(new WordMatchCounter(generateId(), file));
                        }
                    }
                    // 添加任务, 所有子文件作为一个任务
                    tasks.add(new WordMatchCounter(
                            generateId(), filesOfDir, ALL_FILES));

                    // 处理任务
                    invokeAll(tasks);

                    // 收集数据
                    counter = tasks.stream()
                            .mapToInt(ForkJoinTask::join)
                            .sum();
                }
            }
            log.info(methodLog(
                    String.format("Thread %d: %d", getId(), counter)));
            return counter;
        }

        Integer search(File file) {
            Integer counter = 0;
            try (Scanner in = new Scanner(file)) {
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.contains(keyword)) counter++;
                }
            } catch (FileNotFoundException e) {
                log.error(exceptionLog(e));
            }
            log.debug(methodLog(
                    String.format("File %s: %d", file.getPath(), counter)));
            return counter;
        }
    }
}
