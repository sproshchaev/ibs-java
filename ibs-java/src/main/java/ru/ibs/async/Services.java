package ru.ibs.async;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Сквозной пример вебинара: карточка товара собирается из трёх медленных сервисов.
 * Каждый вызов имитирует сетевой запрос и занимает около секунды.
 */
public final class Services {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private Services() {
    }

    public record ProductCard(String sku, double price, int stock, String delivery) {
    }

    public static double price(String sku) {
        log("цена: запрос " + sku);
        sleep(1000);
        return 1990.0;
    }

    public static int stock(String sku) {
        log("остаток: запрос " + sku);
        sleep(1000);
        return 12;
    }

    public static String delivery(String sku) {
        log("доставка: запрос " + sku);
        sleep(1000);
        return "завтра";
    }

    /** Сервис доставки, который отвечает ошибкой. */
    public static String brokenDelivery(String sku) {
        log("доставка: запрос " + sku);
        sleep(300);
        throw new IllegalStateException("сервис доставки недоступен");
    }

    public static void log(String message) {
        System.out.printf("%s [%-28s] %s%n",
                LocalTime.now().format(TIME), Thread.currentThread().getName(), message);
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    public static long since(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}
