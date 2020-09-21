package com.sty.multithread;

/**
 * 手写一个死锁示例
 * 参考：https://www.pianshen.com/article/3676377426/
 * @Author: tian
 * @UpdateDate: 2020/9/21 10:31 AM
 */
public class DeadLockThread {
    public static void main(String[] args) {
        ARunner runnerA = new ARunner();
        BRunner runnerB = new BRunner();
        Thread threadA = new Thread(runnerA);
        Thread threadB = new Thread(runnerB);
        threadA.start();
        threadB.start();

        //Thread-2获取到A锁
        //Thread-3获取到B锁
    }

    //定义2个资源
    private static final Integer a = 0;
    private static final Integer b = 1;

    static class ARunner implements Runnable {
        @Override
        public void run() {
           getA();
        }
    }

    static class BRunner implements Runnable {
        @Override
        public void run() {
            getB();
        }
    }
    public static void getA() {
        //用synchronized 对a对象加锁
        synchronized (a) {
            System.out.println(Thread.currentThread().getName() + "获取到A锁");
            try {
                //等待500ms，再去获取B资源，让另一个线程有时间去独自b
                Thread.sleep(500);
                getB();
                System.out.println(Thread.currentThread().getName() + "获取到B锁");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getB() {
        //用synchronized 对b对象加锁
        synchronized (b) {
            System.out.println(Thread.currentThread().getName() + "获取到B锁");
            try {
                //等待500ms，再去获取A资源，让另一个线程有时间去独自a
                Thread.sleep(500);
                getA();
                System.out.println(Thread.currentThread().getName() + "获取到A锁");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
