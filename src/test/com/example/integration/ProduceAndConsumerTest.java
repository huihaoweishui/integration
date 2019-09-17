package com.example.integration;

import lombok.Data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * @auther 薛晨
 * @date 2019/9/17
 * @time 11:48
 * @description 这不就是消息队列的原型嘛，消费者等待生产者，有一个就处理一个
 */
public class ProduceAndConsumerTest {
    @Data
    public static class Produce implements Runnable {
        private BlockingQueue<String> drop;

        public Produce(BlockingQueue drop) {
            this.drop = drop;
        }

        @Override
        public void run() {
            String[] message = {"hello", "world", "bye"};
            try {
                for (int i = 0; i < message.length; i++) {
                    drop.put(message[i]);
                }
                drop.put("done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @Data
    public static class Consumer implements Runnable {
        private BlockingQueue<String> drop;

        public Consumer(BlockingQueue drop) {
            this.drop = drop;
        }

        @Override
        public void run() {
            try {
                for (String message = drop.take(); !message.equals("DONE"); message = drop.take()) {
                    System.out.format("MESSAGE RECEIVED: %s%n", message);
                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> drop = new SynchronousQueue<>();
        new Thread(new Produce(drop)).start();
        new Thread(new Consumer(drop)).start();
    }
}
