/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.wangsan.study.reactive;


import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * created by wangsan on 2021/08/28.
 *
 * @author wangsan
 */
public class FluxHelloTests {


	@Test
	public void testFlux() {
		Flux<String> flux = Flux.just("foo", "bar");
		StepVerifier.create(flux)
				.expectNext("foo", "bar")
				.verifyComplete();
	}
}