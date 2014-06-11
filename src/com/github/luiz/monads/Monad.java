package com.github.luiz.monads;

import java.util.function.Function;

public interface Monad<Self, T> {
	<U> Monad<? extends Self, U> bind(Function<T, Monad<? extends Self, U>> f);
}
