package com.github.luiz.monads;

import static com.github.luiz.monads.Maybe.some;

import java.util.function.Function;

public class MaybeTests {
	public static void main(String[] args) {
		Maybe<Integer> result = some(5).bind(multiply(2)).bind(sum(2));
		System.out.println(result);
	}

	private static Function<Integer, Maybe<Integer>> multiply(int n) {
		return value -> some(value * n);
	}

	private static Function<Integer, Maybe<Integer>> sum(int n) {
		return value -> some(value + n);
	}
}
