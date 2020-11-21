package com.sty.multithread;

/**
 * 99个线程按顺序打印1-99  --> 注意volatile关键字
 * @Author: tian
 * @Date: 2020/11/22 0:54
 */
public class Print99InOrder {
    private static Object lock = new Object();
    private static volatile int currentNum = 0;

    public static void main(String[] args) {
        for (int i = 1; i < 100; i++) {
            Thread thread = new MyThread(i);
            thread.start();
        }
        synchronized (lock) {
            currentNum++;
            lock.notifyAll();
        }
    }

    public static class MyThread extends Thread {
        private int num;

        public MyThread(int num) {
            super("thread-" + num);
            this.num = num;
        }

        @Override
        public void run() {
            super.run();
            synchronized (lock) {
                while (num != currentNum) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + ": " + num);
                currentNum++;
                lock.notifyAll();
            }
        }
    }
}
