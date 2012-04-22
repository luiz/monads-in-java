package com.github.luiz.monads;

public interface Function<Arg, Ret> {
	Ret apply(Arg a);
}
