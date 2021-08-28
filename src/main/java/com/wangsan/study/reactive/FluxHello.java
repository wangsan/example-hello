/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.wangsan.study.reactive;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by wangsan on 2021/08/28.
 *
 * @author wangsan
 */
public class FluxHello {

	public static void main(String[] args) {
		// create
		Flux<String> foobar = Flux.create(x -> {
			x.next("foo");
			x.next("bar");
			x.complete();
		});

		foobar.subscribe(System.out::println);
		System.out.println("--------------");


		//generate
		Flux.generate(x -> {
			x.next("generate");
			// only once next can be invoke
			x.complete();
		}).subscribe(System.out::println);
		System.out.println("--------------");


		Flux.generate(
				() -> 0,
				(state, sink) -> {
					sink.next("3 x " + state + " = " + 3 * state);
					if (state == 10) {
						sink.complete();
					}
					return state + 1;
				}).subscribe(subPrint());


		// just
		Flux.just("just").subscribe(System.out::println);
		Flux.just("just1", "just2").subscribe(System.out::println);
		System.out.println("--------------");

		print(Flux.just("justjust"));


		// from
		//Flux->Flux
		Flux.from(Flux.just("just", "just1", "just2"))
				.subscribe(System.out::println);
		//Mono->Mono
		Flux.from(Mono.just("just mono")).subscribe(System.out::println);
		System.out.println("--------------");

		Flux.fromArray(new String[] {"arr", "arr", "arr", "arr"})
				.subscribe(System.out::println);

		Set<String> v = new HashSet<>();
		v.add("1");
		v.add("2");
		v.add("3");
		Flux.fromIterable(() -> v.iterator()).subscribe(System.out::println);


		Stream<String> ss = Stream.of("s1", "s2", "s3 only can be used once");
		Flux.fromStream(ss).subscribe(System.out::println);
		System.out.println("--------------");


		// defer
		Flux.defer(() -> Flux.just("lazy", "just1", "just2")).subscribe(System.out::println);
		System.out.println("--------------");

		// interval
		Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).subscribe(System.out::println);
		try {
			TimeUnit.SECONDS.sleep(5);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("--------------");

		// empty
		Flux.empty().subscribe(System.out::println);
		System.out.println("--------------");


		// error
		Flux.error(new RuntimeException()).subscribe(System.out::println, Throwable::printStackTrace);
		System.out.println("--------------");

		// never
		Flux.never().subscribe(System.out::println);


		// range
		Flux.range(0, 5).subscribe(System.out::println, null, () -> System.out.println("range over---"));


		System.out.println("mono ------------");
		Mono.just("mono1").subscribe(System.out::println);

		System.out.println("test ------------");
		Flux<Integer> ints = Flux.range(1, 5)
				.map(i -> {
					if (i != 4) {
						return i;
					}
					throw new RuntimeException("Got to 4");
				});
		ints.subscribe(i -> System.out.println(i), error -> System.err.println("Error: " + error), () -> System.out
				.println("Done"));


		System.out.println("test2 ------------");
		Flux.range(1, 4).subscribe(i -> System.out.println(i),
				error -> System.err.println("Error " + error),
				() -> System.out.println("Done"),
				sub -> sub.request(2));

		System.out.println("test3 ------------");

		Flux.range(1, 4).subscribe(subPrint());


		System.out.println("test4 ------------");
		Flux.range(1, 10)
				.doOnRequest(r -> System.out.println("request of " + r))
				.subscribe(new BaseSubscriber<Integer>() {

					@Override
					public void hookOnSubscribe(Subscription subscription) {
						request(1);
					}

					@Override
					public void hookOnNext(Integer integer) {
						System.out.println("Cancelling after having received " + integer);
						cancel();
					}
				});
	}


	public static void print(Flux<?> flux) {
		flux.subscribe(System.out::println, t -> t.printStackTrace(), () -> System.out.println("------------"));
	}


	public static <T> Subscriber<T> subPrint() {
		return new BaseSubscriber<T>() {
			@Override
			public void hookOnSubscribe(Subscription subscription) {
				System.out.println("Subscribed start");
				request(1);
			}

			@Override
			public void hookOnNext(T t) {
				System.out.println(t);
				request(1);
			}

			@Override
			protected void hookOnComplete() {
				System.out.println("Subscribed end ---------");
			}
		};
	}

}
