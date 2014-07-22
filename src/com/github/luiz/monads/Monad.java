package com.github.luiz.monads;

import java.util.function.Function;

public interface Monad<Self, T> {
	<U extends Monad<? extends Self, ?>> U bind(Function<T, U> f);
}
