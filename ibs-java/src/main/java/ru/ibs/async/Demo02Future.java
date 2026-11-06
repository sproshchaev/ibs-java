package ru.ibs.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static ru.ibs.async.Services.*;

/**
 * Демо 2. Future из Java 5: вызовы идут параллельно, но собрать результат
 * можно только блокирующим get(). Цепочек и обработки ошибок нет.
 */
public class Demo02Future {

    public static void main(String[] args) throws Exception {
        try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
            long start = System.nanoTime();

            Future<Double> price = pool.submit(() -> price("SKU-1"));
            Future<Integer> stock = pool.submit(() -> stock("SKU-1"));
            Future<String> delivery = pool.submit(() -> delivery("SKU-1"));

            log("main ждёт на get()");
            ProductCard card = new ProductCard("SKU-1", price.get(), stock.get(), delivery.get());
            log(card + " за " + since(start) + " мс");
        }
    }
}
