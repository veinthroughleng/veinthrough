package veinthrough.test.async;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.FutureCallback;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.async.LoopRunnable;
import veinthrough.test.AbstractUnitTester;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static com.google.common.util.concurrent.Futures.addCallback;
import static com.google.common.util.concurrent.JdkFutureAdapters.listenInPoolThread;
import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * 使用LoopCallable/LoopRunnable循环执行一个任务(count), 直到THRESHOLD/interrupted,
 * 通过调用pool.shutdownNow()来interrupt pool中的所有任务.
 * @see veinthrough.api.async.LoopRunnable
 * @see veinthrough.api.async.LoopCallable
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 * 1. Without sleep(): No sleep() in while, 使用!Thread.interrupted()
 * 2. With sleep(): With sleep(), no need to use !Thread.interrupted(),
 * as sleep will clear interrupted sign
 * 3. With sleep() and re-interrupt() if InterruptedException,
 * try必须放在while内, 如果try放在while外, 会立刻跳出, !Thread.interrupted()就没有意义
 * 4. canceller: if timeoutOrCondition, stop all tasks in pool
 *   (1) 通过在LoopRunnable中增加finish参数来执行结束操作
 *   @see InterruptTest#interruptTest()
 *   (2) 使用Guava中的Futures.addCallback添加callback来执行结束操作
 *   添加callback: Runnable/Callable -> FutureTask -> Listenable
 *   @see InterruptTest#interruptTest2()
 * </pre>
 */

@SuppressWarnings("Duplicates")
@Slf4j
public class InterruptTest extends AbstractUnitTester {
    private static final int MILLIS_ONE_SECOND = 1000;
    private static final int INTERVAL = MILLIS_ONE_SECOND;
    private static final int THRESHOLD = 10;
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    private int counterWithoutSleep = 0,
            counterWithSleep = 0,
            counterWithSleepAndReinterrupt = 0,
            counterOfCanceller = 0;

    @Override
    public void test() {
    }

    // 4.(1) 通过在LoopRunnable中增加finish参数来执行结束操作
    @Test
    public void interruptTest() throws InterruptedException {
        // noinspection deprecation
        pool.invokeAll(
                ImmutableList.of(
                        // count task without sleep
                        Executors.callable(
                                LoopRunnable.hyper(
                                        () -> {
                                            log.info(methodLog(counterWithoutSleep));
                                            counterWithoutSleep++;
                                        },
                                        // terminator
                                        () -> counterWithoutSleep >= THRESHOLD),
                                null),
                        // count task with sleep
                        Executors.callable(
                                LoopRunnable.sleepyAtInterval(
                                        () -> {
                                            log.info(methodLog(counterWithSleep));
                                            counterWithSleep++;
                                        },
                                        THRESHOLD * MILLIS_ONE_SECOND,
                                        INTERVAL),
                                null),
                        // count task with sleep and re-interrupt
                        Executors.callable(
                                LoopRunnable.sleepyAndInterruptedAtInterval(
                                        () -> {
                                            log.info(methodLog(counterWithSleepAndReinterrupt));
                                            counterWithSleepAndReinterrupt++;
                                        },
                                        THRESHOLD * MILLIS_ONE_SECOND,
                                        INTERVAL),
                                null),
                        // canceller(finish) task
                        // if canceller is timeoutOrCondition, it will stop all tasks in pool
                        // 通过在LoopRunnable中增加finish参数来执行结束操作,
                        // 而不是使用Guava中的Futures.addCallback添加callback来执行结束操作
                        Executors.callable(
                                LoopRunnable.sleepyAtInterval(
                                        // task
                                        () -> {
                                            log.info(methodLog("Canceller " + counterOfCanceller));
                                            counterOfCanceller++;
                                        },
                                        // half time
                                        THRESHOLD / 4 * MILLIS_ONE_SECOND,
                                        INTERVAL / 2,
                                        // finish
                                        () -> {
                                            log.info(methodLog(
                                                    "Starting to stop all actively executing tasks ..."));
                                            pool.shutdownNow();
                                        })
                        )));
        pool.shutdown();
    }

    // 4.(2) 使用Guava中的Futures.addCallback添加callback来执行结束操作
    @Test
    public void interruptTest2() throws InterruptedException {
        // if canceller is timeoutOrCondition, it will stop all tasks in pool
        // 转换成FutureTask是为了添加完成时的callback
        FutureTask<Object> cancellerTask = new FutureTask<>(
                LoopRunnable.sleepyAtInterval(
                        () -> {
                            log.info(methodLog("Canceller " + counterOfCanceller));
                            counterOfCanceller++;
                        },
                        // half time
                        THRESHOLD / 4 * MILLIS_ONE_SECOND,
                        INTERVAL / 2),
                null);

        // noinspection deprecation
        pool.invokeAll(
                ImmutableList.of(
                        // count task without sleep
                        Executors.callable(
                                LoopRunnable.hyper(
                                        () -> {
                                            log.info(methodLog(counterWithoutSleep));
                                            counterWithoutSleep++;
                                        },
                                        // terminator
                                        () -> counterWithoutSleep >= THRESHOLD),
                                null),
                        // count task with sleep
                        Executors.callable(
                                LoopRunnable.sleepyAtInterval(
                                        () -> {
                                            log.info(methodLog(counterWithSleep));
                                            counterWithSleep++;
                                        },
                                        THRESHOLD * MILLIS_ONE_SECOND,
                                        INTERVAL),
                                null),
                        // count task with sleep and re-interrupt
                        Executors.callable(
                                LoopRunnable.sleepyAndInterruptedAtInterval(
                                        () -> {
                                            log.info(methodLog(counterWithSleepAndReinterrupt));
                                            counterWithSleepAndReinterrupt++;
                                        },
                                        THRESHOLD * MILLIS_ONE_SECOND,
                                        INTERVAL),
                                null),
                        // canceller task
                        Executors.callable(cancellerTask),
                        // when canceller task finished, do real cancelling by callback
                        // 这里新建了一个任务(线程)来注册callback
                        // 因为如果在main thread中注册callback, main thread执行会等到所有tasks完成后执行
                        // [?] main抢不到计算资源?
                        Executors.callable(() ->
                                // if canceller is timeoutOrCondition, it will stop all tasks in pool
                                addCallback(
                                        listenInPoolThread(cancellerTask),
                                        new FutureCallback<Object>() {
                                            @Override
                                            public void onFailure(Throwable e) {
                                                log.error(exceptionLog(e));
                                            }

                                            @Override
                                            public void onSuccess(Object value) {
                                                log.info(methodLog("Starting to stop all actively executing tasks ..."));
                                                pool.shutdownNow();
                                            }
                                        }))));
        pool.shutdown();
    }
}
