package com.vincenzo.simple.buffer;

import java.util.LinkedList;
import java.util.Queue;

public class BasicProducerConsumer {
    private final Queue<Integer> buffer = new LinkedList<>();
    private final int capacity = 5;

    // 생산자 메서드
    public synchronized void produce(int value) throws InterruptedException {
        while (buffer.size() == capacity) {
            wait(); // 버퍼가 가득 차면 대기
        }
        buffer.add(value); // 버퍼에 데이터 추가
        System.out.println("Produced: " + value);
        notifyAll(); // 소비자에게 알림
    }

    // 소비자 메서드
    public synchronized int consume() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait(); // 버퍼가 비어 있으면 대기
        }
        int value = buffer.poll(); // 버퍼에서 데이터 제거
        System.out.println("Consumed: " + value);
        notifyAll(); // 생산자에게 알림
        return value;
    }

    public static void main(String[] args) {
        BasicProducerConsumer pc = new BasicProducerConsumer();

        // 생산자 스레드
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    pc.produce(i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 소비자 스레드
        Thread consumer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    pc.consume();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();
    }

}
