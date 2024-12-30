package com.vincenzo.simple.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantFooBar {

    private int n;
    private Lock lock = new ReentrantLock();
    private Condition fooCondition = lock.newCondition();
    private Condition barCondition = lock.newCondition();
    private boolean fooTurn = true;

    public ReentrantFooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (!fooTurn) {
                    fooCondition.await(); // foo의 차례가 아니면 대기
                }
                printFoo.run();
                fooTurn = false; // bar의 차례로 변경
                barCondition.signal(); // bar 깨움
            } finally {
                lock.unlock();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (fooTurn) {
                    barCondition.await(); // bar의 차례가 아니면 대기
                }
                printBar.run();
                fooTurn = true; // foo의 차례로 변경
                fooCondition.signal(); // foo 깨움
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ReentrantFooBar fooBar = new ReentrantFooBar(5);

        Thread t1 = new Thread(() -> {
            try {
                fooBar.foo(() -> System.out.print("foo"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                fooBar.bar(() -> System.out.print("bar"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        t1.start();
        t2.start();
    }
}
