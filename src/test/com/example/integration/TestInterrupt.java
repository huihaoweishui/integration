package com.example.integration;

import org.junit.Test;

/**
 * @auther 薛晨
 * @date 2019/9/16
 * @time 12:43
 * @description 证明 1、中断一个线程并不一定会终止它的任务(可能只是中断一个操作，特别是比如在for循环的地方，try catch的位置很重要会引起不一样的结果
 * 和SimpleThreads类对比看了很久才发现的 try catch位置，另外中断后剩余代码仍然会执行 如果有的话)，
 * 2、中断用interrupt()静态方法会重置线程的中断状态即变成Runnable状态，所以进一步证明第1点，即会继续进行任务，
 * 3、尤其是在循环操作中，如果我们想在线程中断的时候做什么操作的时候，比如提示一下，需要显示判断interrupt()，然后做相应的操作。
 */
public class TestInterrupt {
    public class LoopPrint implements Runnable {
        @Override
        public void run() {
            // 如果try catch 在for循环的外围，情况就完全不同了 注意！！
//            try {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println(i);
                    System.out.println("子线程醒来后的状态： " + Thread.currentThread().getState());
                } catch (InterruptedException e) {
                    System.out.println("子线程被打断interrupt后的状态(应该会被重置所以是Runnable): " + Thread.currentThread().getState());
                }
                /*if (Thread.currentThread().isInterrupted()) {
                    System.out.println("被打断：" + Thread.currentThread().getState());
                }*/
            }
//            } catch (InterruptedException e) {
//                System.out.println("子线程被打断interrupt后的状态(应该会被重置所以是Runnable): " + Thread.currentThread().getState());
//            }
        }
    }

    @Test
    public void test() throws InterruptedException {
        LoopPrint loopPrint = new LoopPrint();
        Thread thread = new Thread(loopPrint);
        thread.start();
        thread.interrupt();
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(1000);
            System.out.println("主线程睡眠看看 子线程的睡眠时状态：" + thread.getState());
        }
        //等待子线程结束 才结束主线程，否则子线程在睡眠期间 主线程就会结束 进而整个程序结束
        thread.join();
        System.out.println("main finish");
    }
}
