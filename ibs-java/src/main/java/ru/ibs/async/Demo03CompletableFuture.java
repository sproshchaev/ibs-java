package ru.ibs.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.ibs.async.Services.*;

/**
 * Демо 3. CompletableFuture: запуск на своём пуле, композиция thenCombine,
 * преобразование thenApply и ожидание группы через allOf.
 */
public class Demo03CompletableFuture {

    public static void main(String[] args) {
        try (ExecutorService io = Executors.newFixedThreadPool(8)) {
            long start = System.nanoTime();

            CompletableFuture<Double> price = CompletableFuture.supplyAsync(() -> price("SKU-1"), io);
            CompletableFuture<Integer> stock = CompletableFuture.supplyAsync(() -> stock("SKU-1"), io);
            CompletableFuture<String> delivery = CompletableFuture.supplyAsync(() -> delivery("SKU-1"), io);

            CompletableFuture<ProductCard> card = price
                    .thenCombine(stock, (p, s) -> new ProductCard("SKU-1", p, s, null))
                    .thenCombine(delivery, (c, d) -> new ProductCard(c.sku(), c.price(), c.stock(), d))
                    .thenApply(c -> {
                        log("собираю карточку");
                        return c;
                    });

            log("main свободен, пока сервисы отвечают");
            log(card.join() + " за " + since(start) + " мс");

            // Группа из десяти карточек: allOf ждёт все задачи сразу
            start = System.nanoTime();
            CompletableFuture<?>[] all = new CompletableFuture<?>[10];
            for (int i = 0; i < all.length; i++) {
                String sku = "SKU-" + i;
                all[i] = CompletableFuture.supplyAsync(() -> price(sku), io);
            }
            CompletableFuture.allOf(all).join();
            log("10 цен на пуле из 8 потоков за " + since(start) + " мс");
        }
    }
}
