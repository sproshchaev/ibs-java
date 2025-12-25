package ru.ibs.example;

import java.util.concurrent.atomic.AtomicBoolean;

public class FixedWithAtomic {
    private static AtomicBoolean flag = new AtomicBoolean(false);

    public static void main(String[] args) throws InterruptedException {
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag.set(true); // Атомарная установка значения
            System.out.println("Flag set to true");
        });

        Thread readerThread = new Thread(() -> {
            while (!flag.get()) { // Атомарное чтение значения
                // Ждем...
            }
            System.out.println("Flag is now true. Exiting.");
        });

        System.out.println("Starting threads...");
        readerThread.start();
        writerThread.start();

        writerThread.join();
        readerThread.join();
        System.out.println("Main thread finished.");
    }
}
