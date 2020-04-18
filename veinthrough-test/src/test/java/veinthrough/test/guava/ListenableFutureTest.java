package veinthrough.test.guava;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;
import veinthrough.test.async.InterruptTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static veinthrough.api.util.MethodLog.*;

/**
 * @author veinthrough
 * <p>---------------------------------------------------------
 * <pre>
 * APIs:
 *   1. get a ListenableFuture:
 *     (1) [static][sync] Futures.immediateFuture(*)/ immediateFailedFuture(Throwable):
 *     ListenableFuture必须用线程来实现才是async,否则就是sync
 *     (2) [static][async] JdkFutureAdapters.listenInPoolThread(Future):
 *     Convert a Future to a ListenableFuture
 *     (3) [async] ListeningExecutorService.submit(Callable/Runnable)
 *   2. get a ListeningExecutorService:
 *   MoreExecutors.listeningDecorator(ExecutorService), decorate ExecutorService as ListeningExecutorService.
 *   3. FutureCallback<T>:
 *   (1) onFailure(Throwable e)
 *   (2) onSuccess(T value)
 *   4. Futures.addCallback(ListenableFuture<T>, FutureCallback<T>)
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests:
 *   1. Run a count task, and cancel it after a while:
 *     (1) Use Futures.immediateFuture() will lead to sync manner, that is,
 *     task-running and cancelling are both executed in main thread,
 *     so cancelling is run after task-running and in-effective.
 *     (2) Use JdkFutureAdapters.listenInPoolThread(Future)
 *   2. Failed with a exception: Futures.immediateFailedFuture(Throwable)
 *   3. Failed with a exception: run a async task throwing a exception.
 *   4. Other tests:
 *     (1) transform Future and add callback
 *     @see InterruptTest#interruptTest2()
 */
@SuppressWarnings("UnstableApiUsage")
@Slf4j
public class ListenableFutureTest extends AbstractUnitTester {
    private static final int COUNTER = 6;
    private static int i, j;
    private static final ExecutorService pool =
            Executors.newCachedThreadPool();
    private static final ListeningExecutorService listenablePool =
            MoreExecutors.listeningDecorator(pool);

    @Override
    public void test() {
    }

    private FutureCallback<Integer> futureCallbackForCount() {
        return new FutureCallback<Integer>() {
            @Override
            public void onFailure(Throwable e) {
                log.info(methodLog(
                        "Failed with " + j
                                + " for " + e.getClass().getSimpleName()));
            }

            @Override
            public void onSuccess(Integer value) {
                log.info(methodLog(
                        "Succeeded with " + value));
            }
        };
    }

    // 1. Run a count task, and cancel it after a while:
    // (1) Use Futures.immediateFuture() will lead to sync manner, that is,
    // task-running and cancelling are both executed in main thread,
    // so cancelling is run after task-running and in-effective.
    @Test
    public void syncCancelTest() {
        ListenableFuture<Integer> future =
                Futures.immediateFuture(count());

        Futures.addCallback(future, futureCallbackForCount());
        waitForCountBySleep();
        future.cancel(true);
    }

    // 1. Run a count task, and cancel it after a while:
    // (2) Use JdkFutureAdapters.listenInPoolThread(Future)
    @Test
    public void asyncCancelTest() {
        ListenableFuture<Integer> future =
                JdkFutureAdapters.listenInPoolThread(
                        pool.submit(this::count));
        pool.shutdown();

        Futures.addCallback(future, futureCallbackForCount());
        waitForCountBySleep();
        future.cancel(true);
    }


    // 2. Failed with a exception: Futures.immediateFailedFuture(Throwable)
    @Test
    public void syncExceptionTest() {
        ListenableFuture<Integer> future =
                Futures.immediateFailedFuture(new ZeroDivisorException());

        Futures.addCallback(future, futureCallbackForCount());
    }

    // 3. Failed with a exception: run a async task throwing a exception.
    @Test
    public void asyncExceptionTest() {
        ListenableFuture<Integer> future =
                listenablePool.submit(this::divide);
        listenablePool.shutdown();

        Futures.addCallback(future, futureCallbackForCount());
    }

    private Integer count() {
        try {
            for (i = 0; i < COUNTER; i++) {
                log.info(methodLog(i));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.info(exceptionLog(e));
        }
        return i;
    }

    private void waitForCountBySleep() {
        try {
            for (j = 0; j < COUNTER / 2; j++) {
                log.info(methodLog(j));
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.info(exceptionLog(e));
        }
    }

    private int divide() throws ZeroDivisorException {
        // convert a runtime-exception to a checked-exception
        throw new ZeroDivisorException();
    }

    private static class ZeroDivisorException extends Exception {
        ZeroDivisorException() {
            this("the divisor can't be 0");
        }

        ZeroDivisorException(String message) {
            super(message);
        }
    }
}
