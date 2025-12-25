package ru.ibs.example;

public class FixedWithSynchronized {
    private static boolean flag = false;
    private static final Object lock = new Object(); // Общий объект-монитор

    public static void main(String[] args) throws InterruptedException {
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                flag = true;
            }
            System.out.println("Flag set to true");
        });

        Thread readerThread = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    if (flag) {
                        break;
                    }
                }
                // Короткая пауза, чтобы не грузить CPU
                try { Thread.sleep(10); } catch (InterruptedException e) {}
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
