package com.github.luiz.monads;

import java.util.function.Function;

public abstract class Maybe<T> implements Monad<Maybe, T> {
	public static <T> Some<T> some(T value) {
		return new Some<>(value);
	}

	public static <T> None<T> none() {
		return new None<>();
	}
}

class Some<T> extends Maybe<T> {

	private final T value;

	Some(T value) {
		this.value = value;
	}

	@Override
	public <U> Some<U> bind(Function<T, Monad<? extends Maybe, U>> f) {
		return (Some<U>) f.apply(value);
	}
}

class None<T> extends Maybe<T> {

	@Override
	public <U> None<U> bind(Function<T, Monad<? extends Maybe, U>> f) {
		return new None<>();
	}

}