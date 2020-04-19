package veinthrough.test.async;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.async.LoopRunnable;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.exception.StreamExceptionTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static veinthrough.api._interface.CheckedFunction.wrapInt;
import static veinthrough.api.util.Constants.MILLIS_PER_SECOND;
import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * Find all keyword matches in files of a directory by Multi-threads co-work through BlockingQueue.
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Find all keyword matches in files of a directory:
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
 * 2. Handle exception in Stream:
 * @see StreamExceptionTest
 * </pre>
 */
@Slf4j
@SuppressWarnings("Duplicates")
public class BlockingQueueTest extends AbstractUnitTester {
    private static File DUMMY = new File("");
    private static final int FILE_QUEUE_SIZE = 10;
    private static final int SEARCH_THREADS = 10;
    private static final BlockingQueue<File> queue = new ArrayBlockingQueue<>(FILE_QUEUE_SIZE);
    private static final String directory =
            "D:\\Cloud\\Projects\\IdeaProjects\\veinthrough\\veinthrough-test\\src\\test\\java\\veinthrough\\test";
    private static final String keyword = "AbstractUnitTester";

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void wordMatchCountTest() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();

        // (1) invoke all tasks
        List<FutureTask<Integer>> searchTasks =
                IntStream.range(0, SEARCH_THREADS)
                        .mapToObj(SearchTask::new)
                        .map(FutureTask::new)
                        .collect(Collectors.toList());
        pool.invokeAll(
                ImmutableList.<Callable<Object>>builder()
                        // file enumeration task
                        .add(Executors.callable(
                                new FileEnumerationTask(queue, new File(directory))))
                        // search tasks
                        .addAll(searchTasks.stream()
                                .map(Executors::callable)
                                .collect(Collectors.toList()))
                        .build());
        pool.shutdown();

        // (2) waiting for finish
        Thread waiting = new Thread(LoopRunnable.sleepyAtInterval(
                () -> {
                },
                pool::isTerminated,
                MILLIS_PER_SECOND,
                () -> {
                    // 收集数据
                    // Exception方式1. CheckedException -> RuntimeException, 将终止程序
                    int counter = searchTasks.stream()
                            // CheckedException -> RuntimeException
                            .mapToInt(wrapInt(FutureTask::get))
                            .sum();
                    log.info(methodLog(
                            "Count", "" + counter,
                            "Largest pool size", "" + ((ThreadPoolExecutor) pool).getLargestPoolSize()));
                }
        ));
        waiting.start();

        // (3) join waiting task
        waiting.join();
    }

    @AllArgsConstructor
    static class FileEnumerationTask implements Runnable {
        // The field DUMMY cannot be declared static in a non-static inner type,
        // unless initialized with a constant expression
        private BlockingQueue<File> queue;
        private File directory;

        void enumerate(File directory) throws InterruptedException {
            File[] files = directory.listFiles();
            if (files != null && files.length != 0) {
                for (File file : files) {
                    if (file.isDirectory()) enumerate(file);
                        // may throw InterruptedException
                    else queue.put(file);
                }
            }
        }

        @Override
        public void run() {
            try {
                if (directory.isDirectory()) enumerate(directory);
                else queue.put(directory);
                queue.put(DUMMY);
            } catch (InterruptedException e) {
                log.error(exceptionLog(e));
            }
        }
    }

    @AllArgsConstructor
    class SearchTask implements Callable<Integer> {
        @Getter
        private long id;

        @Override
        public Integer call() {
            int counter = 0;
            try {
                File file = queue.take();
                while (file != DUMMY) {
                    counter += search(file);
                    file = queue.take();
                }
                queue.put(DUMMY);
            } catch (InterruptedException e) {
                log.error(exceptionLog(e));
            }
            log.info(methodLog(
                    String.format("Thread %d: %d", getId(), counter)));
            return counter;
        }

        private int search(File file) {
            int counter = 0;
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
