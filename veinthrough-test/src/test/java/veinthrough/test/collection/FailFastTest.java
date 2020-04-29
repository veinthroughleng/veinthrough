package veinthrough.test.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.test.AbstractUnitTester;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * This program demonstrate fail-fast/ConcurrentModificationException in Java.
 * <p>---------------------------------------------------------
 * <pre>
 * Comments:
 * 1. 当某一个线程A通过iterator去遍历某集合的过程中，若该集合的内容被(自己/其他线程)改变了;
 * 那么线程A访问集合时，就会抛出ConcurrentModificationException异常，产生fail-fast事件。
 * 2. 具体实现: 调用iterator时会初始化expectedModCount = modCount
 * list的修改操作只会更改modeCount不会更改expectedModCount
 * iterator的修改操作会调用list的修改操作, 比如iterator.remove()会调用list.remove()
 * 但是iterator的修改操作会同时修改expectedModCount和modeCount
 * 3. CopyOnWriteArrayList
 *   (1) copy-on-write: CopyOnWriteArrayList不会抛ConcurrentModificationException，是因为所有改变其内容的操作(add/remove/clear等),
 *   都会copy一份现有数据称为snapshot，在现有数据上修改好，在把原有数据的引用改成指向修改后的数据, 而不是在读的时候copy.
 *   这个快照并不会和外界有任何联系，某个线程在获取迭代器的时候就会拷贝一份，或者说，每一个线程都将获得当前时刻的一个快照,
 *   所以不需要加锁就可以安全的实现遍历
 *   (2) 内部使用了ReentrantLock, 是为了防止并发修改
 * </pre>
 * <p>---------------------------------------------------------
 * <pre>
 * Tests(traverse and modify a list):
 * 1. by foreach: 会出现ConcurrentModificationException, foreach实际上也是调用iterator,
 * list的修改操作只会更改modeCount不会更改expectedModCount
 * 2. by index: list中的元素个数会越来越少, 需要不断获取list.size()
 *    NOTE: 如果是LinkedList而不是ArrayList, 通过index进行random access效率会很低
 *   (1) 使用固定的size会导致IndexOutOfBoundsException
 *   (2) 使用动态的list.size得到的结果不正确[Amy, Carl, Doug, Frances, Gloria], 因为奇数/偶数是不断变化的
 * 3. by Iterator: 不会出现ConcurrentModificationException
 * 使用list.iterator()/listIterator()效果一样， 都不会引起ConcurrentModificationException
 * iterator的修改操作会同时修改expectedModCount和modeCount
 * 4. by Stream: 出现ConcurrentModificationException, stream本质上也是调用foreach/iterator
 * 5. multi-thread:
 *   (1) 使用ArrayList, 2个任务交叉执行会出现ConcurrentModificationException
 *   (2) 使用CopyOnWriteArrayList, 2个任务交叉执行也不会出现ConcurrentModificationException
 *   NOTE: CopyOnWriteArrayList.COWIterator中add/set/remove都未实现, 抛出UnsupportedOperationException
 * 6. merge test: 将2个列表间断合并, 最好使用LinkedList
 *   LinkedList擅长于修改, ArrayList擅长于random access, 效率最高
 * </pre>
 */
@SuppressWarnings("SuspiciousListRemoveInLoop")
@Slf4j
public class FailFastTest extends AbstractUnitTester {
    @Override
    public void test() {
    }

    /**
     * 1. iterateAndRemoveByForeachTest: 会出现ConcurrentModificationException, foreach实际上也是调用iterator,
     * list的修改操作只会更改modeCount不会更改expectedModCount
     */
    @Test
    public void iterateAndRemoveByForeachTest() {
        List<String> list = Lists.newArrayList(
                "Amy", "Bob", "Carl", "Doug", "Erica", "Frances", "Gloria");
        // delete even-index item
        boolean shouldDelete = false;
        for (String str : list) {
            if (shouldDelete) {
                list.remove(str);
            }
            shouldDelete = !shouldDelete;
        }
        log.info(methodLog(list.toString()));
    }

    /**
     * 2. by index: list中的元素个数会越来越少, 需要不断获取list.size()
     * 如果是LinkedList而不是ArrayList, 通过index进行random access效率会很低
     * (1) 使用固定的size会导致IndexOutOfBoundsException
     */
    @Test
    public void iterateAndRemoveByIndexTest1() {
        List<String> list = Lists.newArrayList(
                "Amy", "Bob", "Carl", "Doug", "Erica", "Frances", "Gloria");
        // delete even-index item
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i % 2 != 0) {
                list.remove(i);
            }
        }
        log.info(methodLog(list.toString()));
    }

    /**
     * 2. by index: list中的元素个数会越来越少, 需要不断获取list.size()
     * 如果是LinkedList而不是ArrayList, 通过index进行random access效率会很低
     * (2) 使用动态的list.size得到的结果不正确[Amy, Carl, Doug, Frances, Gloria], 因为奇数/偶数是不断变化的
     */
    @Test
    public void iterateAndRemoveByIndexTest2() {
        List<String> list = Lists.newArrayList(
                "Amy", "Bob", "Carl", "Doug", "Erica", "Frances", "Gloria");
        // delete even-index item
        for (int i = 0; i < list.size(); i++) {
            if (i % 2 != 0) {
                list.remove(i);
            }
        }
        log.info(methodLog(list.toString()));
    }

    /**
     * by Iterator: 不会出现ConcurrentModificationException
     * 使用list.iterator()/listIterator()效果一样， 都不会引起ConcurrentModificationException
     * iterator的修改操作会同时修改expectedModCount和modeCount
     */
    @Test
    public void iterateAndRemoveByIteratorTest() {
        List<String> list = Lists.newArrayList(
                "Amy", "Bob", "Carl", "Doug", "Erica", "Frances", "Gloria");
        Iterator iterator = list.iterator();
//        Iterator iterator = list.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            if (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
        log.info(methodLog(list.toString()));
    }

    /**
     * by stream: 出现ConcurrentModificationException, stream本质上也是调用foreach/iterator
     */
    @Test
    public void iterateAndRemoveByStreamTest() {
        List<Integer> list = IntStream.range(0, 1000)
                .boxed()
                .collect(Collectors.toList());
        list.forEach(number -> {
            if (number % 2 != 0) list.remove(number);
        });
    }

    /**
     * multi-thread:
     * (1) 使用ArrayList, 2个任务交叉执行会出现ConcurrentModificationException
     */
    @Test
    public void mulThreadsTraverseAndExtendTest() throws InterruptedException {
        List<String> list = Lists.newArrayList(
                "Amy ", "Bob ", "Carl ", "Doug ", "Erica ", "Frances ", "Gloria ");
        ExecutorService pool = Executors.newCachedThreadPool();
        // 2个任务交叉执行会出现ConcurrentModificationException
        pool.invokeAll(ImmutableList.of(
                // 注意lambda会抑制ConcurrentModificationException
//                Executors.callable(() -> list.forEach(System.out::println)),
                Executors.callable(() -> printList(list)),
                Executors.callable(() -> extendList(list))));
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        log.info(methodLog(list.toString()));
    }

    /**
     * multi-thread:
     * (2) 使用CopyOnWriteArrayList, 2个任务交叉执行也不会出现ConcurrentModificationException
     */
    @Test
    public void mulThreadsTraverseAndExtendTest2() throws InterruptedException {
        // NOTE: CopyOnWriteArrayList.COWIterator中add/set/remove都未实现, 抛出UnsupportedOperationException
        List<String> list = Lists.newCopyOnWriteArrayList(Lists.newArrayList(
                "Amy ", "Bob ", "Carl ", "Doug ", "Erica ", "Frances ", "Gloria "));
        ExecutorService pool = Executors.newCachedThreadPool();
        // 2个任务交叉执行也不会出现ConcurrentModificationException
        pool.invokeAll(ImmutableList.of(
                // lambda会抑制ConcurrentModificationException
//                Executors.callable(() -> list.forEach(System.out::println)),
                Executors.callable(() -> printList(list)),
                Executors.callable(() -> extendList(list))));
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
        log.info(methodLog(list.toString()));
    }

    private static void printList(List<String> list) {
        try {
            for (String str : list) {
                log.info(methodLog(str));
            }
            // 必须使用try...catch来捕获ConcurrentModificationException
            // 否则会被lambda抑制
        } catch (Exception e) {
            log.error(exceptionLog(e, "Exit..."));
            // System.exit()会终止整个进程, ConcurrentModificationException(UncheckedException)只会终止当前线程
            System.exit(1);
        }
    }

    private static void extendList(List<String> list) {
        Lists.newArrayList(
                "Hale", "Ian", "Jack", "Kalen", "Lakin")
                .forEach(item -> {
                    list.add(item);
                    log.info(methodLog(list.toString()));
                });
    }


    /**
     * 将2个列表间断合并, 最好使用LinkedList
     * LinkedList擅长于修改, ArrayList擅长于random access, 效率最高
     */
    @Test
    public void mergeTest() {
        List<String> list1 =
                Lists.newLinkedList(
                        Lists.newArrayList("Amy", "Carl", "Erica"));
        List<String> list2 =
                Lists.newLinkedList(
                        Lists.newArrayList("Bob", "Doug", "Frances", "Gloria"));
        // 使用iterator就不会出现ConcurrentModificationException
        ListIterator<String> iter1 = list1.listIterator();
        for (String s : list2) {
            if (iter1.hasNext()) iter1.next();
            // listIterator才有add()
            iter1.add(s);
        }
        log.info(methodLog(list1.toString()));
    }
}
