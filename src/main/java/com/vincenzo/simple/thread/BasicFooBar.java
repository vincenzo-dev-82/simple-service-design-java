package com.vincenzo.simple.thread;

public class BasicFooBar {

    private int n;
    private boolean fooTurn = true; // true면 foo 출력, false면 bar 출력

    public BasicFooBar(int n) {
        this.n = n;
    }

    public synchronized void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (!fooTurn) {
                wait(); // foo의 차례가 아닐 때 대기
            }
            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();
            fooTurn = false; // 다음 차례는 bar
            notifyAll(); // 대기 중인 스레드를 깨움
        }
    }

    public synchronized void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (fooTurn) {
                wait(); // bar의 차례가 아닐 때 대기
            }
            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            fooTurn = true; // 다음 차례는 foo
            notifyAll(); // 대기 중인 스레드를 깨움
        }
    }

    public static void main(String[] args) {
        BasicFooBar fooBar = new BasicFooBar(5);

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
