# 多线程示例

[TOC]

## 一、手写实现

### 1.1 手写死锁

多个并发进程因争夺系统资源二产生相互等待的现象称为**死锁**。

造成死锁需要满足**四个**条件：

> 1. 互斥条件(`Mutual exclusion`)：资源不能被共享，只能由一个进程使用；
> 2. 请求与保持条件(`Hold and wait`)：已经得到资源的进程可以再次申请新的资源；
> 3. 非剥夺条件(`Ne pre-emption`)：已经分配的资源不能从相应的进程中被强制地剥夺；
> 4. 循环等待条件(`Circular wait`)：系统中若干进程组成环路，该环路中每个进程都在等待相邻进程正在占用的资源。

常用的解决死锁的方法：

> 1. 抢占资源：从一个或多个进程中抢占足够数量的资源分配给死锁进程，以解除死锁状态；
> 2. 终止/撤销进程：终止或撤销系统中的一个或多个死锁进程，直至打破死锁状态；
> 3. 终止所有的死锁进程：这种方式简单粗暴，但是代价很大，很有可能会导致一些已经运行了很久的进程前功尽弃；
> 4. 逐个终止进程，直至死锁状态解除：该方法的代价也很大，因为每终止一个进程就需要使用死锁检测来检测系统当前是否处于死锁状态。另外每次终止进程的时候终止哪个进程呢？每次都应该采用最优策略来选择一个代价最小的进程来解除死锁状态。

一般根据以下几个方面来决定终止哪个进程：

> 1. 进程的优先级；
> 2. 进程已经运行的时间以及运行完成还需要的时间；
> 3. 进程已占用系统资源；
> 4. 进程运行完成还需要的资源；
> 5. 终止进程数目；
> 6. 进程是交互还是批处理。

死锁示例：

```java
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
```

在`IDEA`控制台点击`Dump Threads`图标可以查看死锁日志：

![image](https://github.com/tianyalu/MultiThread/raw/master/show/dead_lock_console.png)