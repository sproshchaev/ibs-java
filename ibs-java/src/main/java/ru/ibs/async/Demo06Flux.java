package ru.ibs.async;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static ru.ibs.async.Services.*;

/**
 * Демо 6. Flux: каталог из 20 товаров, flatMap с ограничением параллельности
 * и limitRate — подписчик сам задаёт темп запросов к источнику.
 */
public class Demo06Flux {

    public static void main(String[] args) {
        long start = System.nanoTime();

        Long count = Flux.range(1, 20)
                .map(i -> "SKU-" + i)
                .flatMap(sku -> Mono.fromCallable(() -> price(sku))
                        .subscribeOn(Schedulers.boundedElastic()), 5)
                .count()
                .block();
        log(count + " цен, не больше 5 запросов одновременно, за " + since(start) + " мс");

        // limitRate: подписчик просит у источника по 4 элемента
        Flux.range(1, 10)
                .doOnRequest(n -> log("запрошено у источника: " + n))
                .limitRate(4)
                .doOnNext(i -> log("обработан элемент " + i))
                .blockLast();
    }
}
