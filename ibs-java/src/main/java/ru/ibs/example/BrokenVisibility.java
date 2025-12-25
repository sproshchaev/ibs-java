package ru.ibs.example;

public class BrokenVisibility {

    // private static boolean flag = false; // Сломанная версия
    private static volatile boolean flag = false; // Починенная версия 1

    public static void main(String[] args) throws InterruptedException {
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Имитируем работу
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true; // Поток-писатель меняет флаг
            System.out.println("Flag set to true");
        });

        Thread readerThread = new Thread(() -> {
            while (!flag) {
                // Бесконечный цикл? Поток может не увидеть изменение флага!
            }
            System.out.println("Flag is now true. Exiting.");
        });

        System.out.println("Starting threads...");
        readerThread.start();
        writerThread.start();

        writerThread.join();
        readerThread.join(); // В сломанной версии readerThread никогда не завершится
        System.out.println("Main thread finished.");
    }
}