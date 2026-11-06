package ru.ibs.async;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

import static ru.ibs.async.Services.*;

/**
 * Демо 5. Та же карточка на Project Reactor: Mono ленивый,
 * zip собирает три источника, тайм-аут и запасное значение задаются операторами.
 */
public class Demo05Reactor {

    static Mono<Double> priceMono(String sku) {
        return Mono.fromCallable(() -> price(sku)).subscribeOn(Schedulers.boundedElastic());
    }

    static Mono<Integer> stockMono(String sku) {
        return Mono.fromCallable(() -> stock(sku)).subscribeOn(Schedulers.boundedElastic());
    }

    static Mono<String> deliveryMono(String sku) {
        return Mono.fromCallable(() -> delivery(sku)).subscribeOn(Schedulers.boundedElastic());
    }

    public static void main(String[] args) {
        Mono<ProductCard> card = Mono.zip(priceMono("SKU-1"), stockMono("SKU-1"), deliveryMono("SKU-1"))
                .map(t -> new ProductCard("SKU-1", t.getT1(), t.getT2(), t.getT3()));

        log("Mono описан, но ни один сервис ещё не вызван");
        sleep(500);

        long start = System.nanoTime();
        log(card.block() + " за " + since(start) + " мс");

        // Тайм-аут и запасное значение — два оператора в цепочке
        String delivery = deliveryMono("SKU-1")
                .timeout(Duration.ofMillis(500))
                .onErrorReturn("срок уточняется")
                .block();
        log("доставка = " + delivery);
    }
}
