package veinthrough.test.async;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import veinthrough.api.async.LoopRunnable;
import veinthrough.test.AbstractUnitTester;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static veinthrough.api.util.Constants.MILLIS_PER_SECOND;
import static veinthrough.api.util.MethodLog.exceptionLog;
import static veinthrough.api.util.MethodLog.methodLog;

/**
 * @author veinthrough
 * <p>
 * Simulate a bank and randomly transfer from one account to another:
 * (1) by ReentrantLock and Condition
 * @see SyncBankTest#syncBankTest1()
 * (2) by inner object lock and condition
 * use synchronized instead of ReentrantLock, 使用synchronized, 那么对象的内部所将保护整个方法
 * alling wait() is equals to calling await() of intrinsic lock
 * calling notifyAll() is equals to calling signalAll() of intrinsic lock
 * @see SyncBankTest#syncBankTest2()
 */
@SuppressWarnings("Duplicates")
@Slf4j
public class SyncBankTest extends AbstractUnitTester {
    private static final int NACCOUNTS = 3;
    private static final double INITIAL_BALANCE = 1000;
//    private static final long TIME_OUT = 10 * MILLIS_PER_SECOND;

    /* (non-Javadoc)
     * @see UnitTester#test()
     */
    @Override
    public void test() {
    }

    @Test
    public void syncBankTest1() throws InterruptedException {
        _syncBankTest(new Bank1(NACCOUNTS, INITIAL_BALANCE));
    }

    @Test
    public void syncBankTest2() throws InterruptedException {
        _syncBankTest(new Bank2(NACCOUNTS, INITIAL_BALANCE));
    }

    // 总金额NACCOUNTS * INITIAL_BALANCE
    // 每次至多转账INITIAL_BALANCE, 每一时刻至少有一个账户>=INITIAL_BALANCE
    // 1. 如果没有timeout, 不会出现死锁
    // 2. 有timeout, 当账户很少时, 容易出现死锁, 比如1/2都余额不足, 3余额足但是已经退出
    private void _syncBankTest(Bank bank) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        // [?] 在IntStream中使用pool.submit()线程不明原因的停止
        // [?] 而使用pool.invokeAll()就不会, IntStream中submit太快?
        pool.invokeAll(IntStream.range(0, NACCOUNTS)
                .mapToObj(from ->
                        Executors.callable(LoopRunnable.sleepyAtInterval(
                                () -> {
                                    try {
                                        bank.transfer(from,
                                                // random to
                                                (int) (bank.getSize() * Math.random()),
                                                // random amount
                                                INITIAL_BALANCE * Math.random());
                                    } catch (InterruptedException e) {
                                        log.error(exceptionLog(e));
                                    }
                                },
                                // 加上timeout, 当账户很少时, 容易出现死锁, 比如1/2都余额不足, 3余额足但是已经退出
//                                TIME_OUT,
                                // random interval
                                (long) (MILLIS_PER_SECOND * Math.random())
                        )))
                .collect(Collectors.toList()));

        pool.shutdown();
    }

    interface Bank {
        int getSize();
        void transfer(int from, int to, double amount) throws InterruptedException;
    }


    class Bank1 implements Bank{
        private final double[] accounts;
        private Lock bankLock;
        private Condition sufficientFunds;
        @Getter
        private int size;

        /**
         * Constructs the bank.
         *
         * @param n              the number of accounts
         * @param initialBalance the initial balance for each account
         */
        Bank1(int n, double initialBalance) {
            this.size = n;
            accounts = new double[n];
            Arrays.fill(accounts, initialBalance);

            // reentrant lock
            bankLock = new ReentrantLock();
            // one condition of lock
            sufficientFunds = bankLock.newCondition();
        }

        /**
         * Transfers money from one account to another.
         *
         * @param from   the account to transfer from
         * @param to     the account to transfer to
         * @param amount the amount to transfer
         * @throws InterruptedException exception
         */
        @Override
        public void transfer(int from, int to, double amount) throws InterruptedException {
            log.debug(methodLog(0,
                    "from", "" + from,
                    "to", "" + to,
                    "amount", "" + amount));
            if (from == to) {
                return;
            }

            // (1) get lock and lock
            log.debug(methodLog(1,
                    "Manually lock"));
            bankLock.lock();

            // 虽然没有exception, 使用try ... finally来保证unlock
            try {
                // (2) insufficient funds
                // await may throw InterruptedException
                while (accounts[from] < amount) {
                    log.debug(methodLog(2,
                            "Inefficient funds",
                            "accounts[from]", "" + accounts[from],
                            "amount", "" + amount));
                    sufficientFunds.await();
                }

                // (3) sufficient funds
                log.debug(methodLog(3,
                        "Efficient funds",
                        "accounts[from]", "" + accounts[from],
                        "amount", "" + amount));

                // (4) transfer
                log.info(methodLog(4,
                        Thread.currentThread().getName(),
                        String.format("Transfer %10.2f from %d to %d", amount, from, to)));
                accounts[from] -= amount;
                accounts[to] += amount;

                // (5) signal all as it can make others to be with sufficient funds
                log.info(methodLog(5,
                        "Accounts", Arrays.toString(accounts),
                        "Total Balance", String.format("%10.2f%n", getTotalBalance())));
                sufficientFunds.signalAll();
            } finally {
                // (6) unlock
                log.debug(methodLog(6,
                        "Manually unlock"));
                bankLock.unlock();
            }
        }

        /**
         * Gets the sum of all account balances.
         *
         * @return the total balance
         */
        private double getTotalBalance() {
            // the bankLock is re-entrant
            bankLock.lock();

            // 虽然没有exception, 使用try ... finally来保证unlock
            try {
                return DoubleStream.of(accounts).sum();
            } finally {
                bankLock.unlock();
            }
        }
    }

    class Bank2 implements Bank{
        private final double[] accounts;
        @Getter
        private int size;

        /**
         * Constructs the bank.
         *
         * @param n              the number of accounts
         * @param initialBalance the initial balance for each account
         */
        Bank2(int n, double initialBalance) {
            this.size = n;
            accounts = new double[n];
            Arrays.fill(accounts, initialBalance);
        }

        /**
         * Transfers money from one account to another.
         *
         * @param from   the account to transfer from
         * @param to     the account to transfer to
         * @param amount the amount to transfer
         * @throws InterruptedException exception
         */
        // use synchronized instead of ReentrantLock
        // calling wait() is equals to calling await() of intrinsic lock
        // calling notifyAll() is equals to calling signalAll() of intrinsic lock
        @Override
        public synchronized void transfer(int from, int to, double amount) throws InterruptedException {
            // (1) get lock
            log.debug(methodLog(1,
                    "Automatically lock",
                    "from", "" + from,
                    "to", "" + to,
                    "amount", "" + amount));
            if (from == to) {
                return;
            }

            // (2) insufficient funds
            // await may throw InterruptedException
            if (accounts[from] < amount) {
                log.debug(methodLog(2,
                        "Inefficient funds",
                        "accounts[from]", "" + accounts[from],
                        "amount", "" + amount));
                wait();
            }

            // (3) sufficient funds
            log.debug(methodLog(3,
                    "Efficient funds",
                    "accounts[from]", "" + accounts[from],
                    "amount", "" + amount));

            // (4) transfer
            log.info(methodLog(4,
                    Thread.currentThread().getName(),
                    String.format("Transfer %10.2f from %d to %d", amount, from, to)));
            accounts[from] -= amount;
            accounts[to] += amount;

            // (5) signal all as it can make others to be with sufficient funds
            log.info(methodLog(5,
                    "Accounts", Arrays.toString(accounts),
                    "Total Balance", String.format("%10.2f%n", getTotalBalance())));
            notifyAll();

            // (6) unlock
            log.debug(methodLog(6,
                    "Automatically unlock"));
        }

        /**
         * Gets the sum of all account balances.
         *
         * @return the total balance
         */
        // use synchronized
        private synchronized double getTotalBalance() {
            // 虽然没有exception, 使用try ... finally来保证unlock
            return DoubleStream.of(accounts).sum();
        }
    }
}
