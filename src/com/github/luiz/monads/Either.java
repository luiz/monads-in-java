package com.github.luiz.monads;

import java.util.function.Function;

public abstract class Either<A, B> implements Monad<Either, B> {
	public static <A, B> Left<A, B> left(A value) {
		return new Left<A, B>(value);
	}
	public static <A, B> Right<A, B> right(B value) {
		return new Right<A, B>(value);
	}
}

class Left<A, B> extends Either<A, B> {
	private final A value;

	public Left(A value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U extends Monad<? extends Either, ?>> U bind(Function<B, U> f) {
		return (U) this;
	}

	@Override
	public String toString() {
		return "Left[" + value + "]";
	}
}

class Right<A, B> extends Either<A, B> {
	private final B value;

	public Right(B value) {
		this.value = value;
	}

	@Override
	public <U extends Monad<? extends Either, ?>> U bind(Function<B, U> f) {
		return f.apply(value);
	}

	@Override
	public String toString() {
		return "Right[" + value + "]";
	}
}
