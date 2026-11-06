package ru.ibs.async;

import static ru.ibs.async.Services.*;

/**
 * Демо 1. Синхронная сборка карточки: три вызова идут друг за другом,
 * время ответа равно сумме задержек.
 */
public class Demo01Blocking {

    public static void main(String[] args) {
        long start = System.nanoTime();

        double price = price("SKU-1");
        int stock = stock("SKU-1");
        String delivery = delivery("SKU-1");

        ProductCard card = new ProductCard("SKU-1", price, stock, delivery);
        log(card + " за " + since(start) + " мс");
    }
}
