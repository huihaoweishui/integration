package com.example.integration;

import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther 薛晨
 * @date 2019/8/26
 * @time 10:26
 * @description
 */

public class MyTest {
    @Data
    private class ThreadTask implements Runnable {
        // 线程任务的唯一标识
        private int threadTaskId;

        @Override
        public void run() {
            System.out.println("this a thread task :" + threadTaskId + ", not a thread!");
        }
    }

    @Data
    private class TaskCall implements Callable<Integer> {

        private ThreadTask task;

        @Override
        public Integer call() throws Exception {
            return task.getThreadTaskId();
        }

        public TaskCall(ThreadTask task) {
            this.task = task;
        }
    }

    @Test
    public void testThread() throws ExecutionException, InterruptedException {

        Runnable a = () -> System.out.println("hehe");
        new Thread(a).start();
        ThreadTask myThreadTask = new ThreadTask();
        myThreadTask.setThreadTaskId(1);
        ThreadTask myThreadTask2 = new ThreadTask();
        myThreadTask2.setThreadTaskId(2);
        new Thread(myThreadTask).start();
        Thread thread1 = Thread.currentThread();
        System.out.println(thread1.getId());
        new Thread(myThreadTask2).start();
        //
        FutureTask<Integer> futureTask = new FutureTask<Integer>(new TaskCall(myThreadTask));
        FutureTask<Integer> futureTask2 = new FutureTask<Integer>(new TaskCall(myThreadTask2));
        new Thread(futureTask).start();
        new Thread(futureTask2).start();
        Integer integer = futureTask.get();
        Integer integer1 = futureTask2.get();
        System.out.println(integer + ": " + integer1);
        Thread thread = Thread.currentThread();
        System.out.println(thread.getId());
    }

    public static class Bank {
        private int[] accounts = {1000, 1000, 1000, 1000, 1000};
        private Lock bankLock = new ReentrantLock();
        private Condition condition = bankLock.newCondition();

        public Bank() {
        }

        public void transfer(int from, int to, int amount) {
            bankLock.lock();
            try {
                while (accounts[from] < amount) {
                    condition.await();
                    System.out.println(Thread.currentThread() + " " + Thread.currentThread().getState() + " await");
                }
                System.out.print(Thread.currentThread());
                accounts[from] -= amount;
                System.out.println("from:" + from + " to: " + to + " amount:" + amount);
                accounts[to] += amount;
                System.out.println("fromLeft:" + accounts[from] + " toLeft: " + accounts[to]);
                System.out.println(" Total Balance: " + getTotal());
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bankLock.unlock();
            }
        }

        private int getTotal() {
            bankLock.lock();
            try {
                int left = 0;
                for (int i = 0; i < accounts.length; i++) {
                    left += accounts[i];
                }
                return left;
            } finally {
                bankLock.unlock();
            }

        }
    }

    @Test
    public void test() {
        Bank bank = new Bank();
        int[] accounts = bank.accounts;
        for (int account : accounts) {
            Runnable runnable = () -> {
                while (true) {
                    System.out.println("currentThread: " + Thread.currentThread() + " state:" + Thread.currentThread().getState());
                    int from = new Random().nextInt(accounts.length);
                    int to = new Random().nextInt(accounts.length);
                    int amount = new Random().nextInt(100);
                    bank.transfer(from, to, amount);
                }
            };
            Thread a = new Thread(runnable);
            a.start();
        }
    }

    /**
     * 单元测试 会正常exit看不完全 ，main方法不会(看了半天 还以为自己逻辑写错了。。。)。具体原因未知，大概测试终究是测试，main才是实际
     *
     * @param args
     */
    public static void main(String[] args) {
        Bank bank = new Bank();
        int[] accounts = bank.accounts;
        List<Thread> threads = new ArrayList<>();
        for (int account : accounts) {
            Runnable runnable = () -> {
                while (true) {
                    System.out.println("currentThread: " + Thread.currentThread() + " state:" + Thread.currentThread().getState());
                    int from = new Random().nextInt(accounts.length);
                    int to = new Random().nextInt(accounts.length);
                    int amount = new Random().nextInt(100);
                    bank.transfer(from, to, amount);
                }
            };
            Thread a = new Thread(runnable);
            a.start();
            threads.add(a);
        }
    }

    public static class Bank2 {
        private int[] accounts = {1000, 1000, 1000, 1000, 1000};

        public Bank2() {
        }

        public synchronized void transfer(int from, int to, int amount) {
            try {
                while (accounts[from] < amount) {
                    wait();
                    System.out.println(Thread.currentThread() + " " + Thread.currentThread().getState() + " await");
                }
                System.out.print(Thread.currentThread());
                accounts[from] -= amount;
                System.out.println("from:" + from + " to: " + to + " amount:" + amount);
                accounts[to] += amount;
                System.out.println("fromLeft:" + accounts[from] + " toLeft: " + accounts[to]);
                System.out.println(" Total Balance: " + getTotal());
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
            }
        }

        private synchronized int getTotal() {
            try {
                int left = 0;
                for (int i = 0; i < accounts.length; i++) {
                    left += accounts[i];
                }
                return left;
            } finally {
            }

        }
    }
}
