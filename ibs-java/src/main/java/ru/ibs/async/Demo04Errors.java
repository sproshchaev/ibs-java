package ru.ibs.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ru.ibs.async.Services.*;

/**
 * Демо 4. Ошибки и тайм-ауты в CompletableFuture: exceptionally,
 * handle и completeOnTimeout. Сбой доставки не роняет карточку.
 */
public class Demo04Errors {

    public static void main(String[] args) {
        try (ExecutorService io = Executors.newFixedThreadPool(4)) {
            // 1. exceptionally: запасное значение при ошибке
            String d1 = CompletableFuture.supplyAsync(() -> brokenDelivery("SKU-1"), io)
                    .exceptionally(ex -> {
                        log("exceptionally: " + ex.getCause().getMessage());
                        return "срок уточняется";
                    })
                    .join();
            log("доставка = " + d1);

            // 2. handle: один обработчик и для результата, и для ошибки
            String d2 = CompletableFuture.supplyAsync(() -> brokenDelivery("SKU-1"), io)
                    .handle((value, ex) -> ex == null ? value : "самовывоз")
                    .join();
            log("доставка = " + d2);

            // 3. completeOnTimeout: сервис медленнее 500 мс, отдаём значение по умолчанию
            long start = System.nanoTime();
            String d3 = CompletableFuture.supplyAsync(() -> delivery("SKU-1"), io)
                    .completeOnTimeout("срок уточняется", 500, TimeUnit.MILLISECONDS)
                    .join();
            log("доставка = " + d3 + " за " + since(start) + " мс");
        }
    }
}
