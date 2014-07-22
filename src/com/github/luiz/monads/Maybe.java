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
	public <U extends Monad<? extends Maybe, ?>> U bind(Function<T, U> f) {
		return f.apply(value);
	}

	@Override
	public String toString() {
		return "Some[" + value + "]";
	}
}

class None<T> extends Maybe<T> {

	@Override
	public <U extends Monad<? extends Maybe, ?>> U bind(Function<T, U> f) {
		return (U) this;
	}

	@Override
	public String toString() {
		return "None[]";
	}
}