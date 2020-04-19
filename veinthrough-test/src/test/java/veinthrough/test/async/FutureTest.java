package veinthrough.test.async;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import veinthrough.api.generic.Either;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.exception.StreamExceptionTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static veinthrough.api._interface.CheckedFunction.liftWithValue;
import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * Find all keyword matches in files of a directory by recursively invoke FutureTasks.
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
public class FutureTest extends AbstractUnitTester {
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
    public void wordMatchCountTest() throws ExecutionException, InterruptedException {
        // FutureTask是Future/Runnable的中转站
        FutureTask<Integer> task = new FutureTask<>(
                new WordMatchCounter(0, new File(directory)));
        new Thread(task).start();
        log.info(methodLog(
                // task.get() is block
                "Matches", String.valueOf(task.get())));
    }

    @SuppressWarnings({"Duplicates", "unchecked", "OptionalGetWithoutIsPresent"})
    @AllArgsConstructor
    public static class WordMatchCounter implements Callable<Integer> {
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
        public Integer call() {
            int counter = 0;
            if (files != null && files.size() != 0) {
                if (allFiles) {
                    counter = files.stream()
                            .mapToInt(this::search)
                            .sum();
                } else {
                    List<FutureTask<Integer>> tasks = new ArrayList<>();
                    List<File> filesOfDir = new ArrayList<>();
                    for (File file : files) {
                        if (!file.isDirectory()) {
                            filesOfDir.add(file);
                        } else {
                            // 添加任务, 每个子目录作为一个任务
                            tasks.add(new FutureTask<>(
                                    new WordMatchCounter(generateId(), file)));
                        }
                    }
                    // 添加任务, 所有子文件作为一个任务
                    tasks.add(new FutureTask<>(
                            new WordMatchCounter(generateId(), filesOfDir, ALL_FILES)));

                    // 处理任务
                    // Exception方式1. CheckedException -> RuntimeException, 将终止程序
//                    counter = tasks.stream()
//                            .peek(task -> new Thread(task).start())
//                            // CheckedException -> RuntimeException
//                            .mapToInt(wrapInt(FutureTask::get))
//                            .sum();

                    // 处理任务
                    // Exception方式2. CheckedException -> Either.left, 将忽略Exception并计算最终结果
//                    counter = tasks.stream()
//                            .peek(task -> new Thread(task).start())
//                            // CheckedException -> Either.left
//                            .map(liftWithValue(FutureTask::get))
//                            .filter(Either::isRight)
//                            .mapToInt(either -> (int) either.getRight().get())
//                            .sum();

                    // 处理任务
                    // Exception方式3. CheckedException/task -> Either.left, 将打印Exception并计算最终结果
                    Map<Boolean, List<Either>> results = tasks.stream()
                            // 每个任务作为一个thread运行
                            .peek(task -> new Thread(task).start())
                            // 忽略exception
                            .map(liftWithValue(FutureTask::get))
                            .collect(Collectors.partitioningBy(Either::isLeft));
                    // 打印出错的任务/exception
                    results.get(true)
                            .forEach(either -> {
                                Pair<Throwable, FutureTask<Integer>> taskAndException =
                                        (Pair<Throwable, FutureTask<Integer>>) either.getLeft().get();
                                log.warn(exceptionLog(
                                        taskAndException.getLeft(),
                                        "task", taskAndException.getRight().toString()));
                            });
                    // 收集数据
                    counter = results.get(false).stream()
                            .mapToInt(either -> (int) either.getRight().get())
                            .sum();
                }
            }
            log.info(methodLog(
                    String.format("Thread %d: %d", getId(), counter)));
            return counter;
        }

        Integer search(File file) {
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
