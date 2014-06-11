package com.github.luiz.monads;

import java.util.function.Function;

public class State<S, T> {

	private final Function<S, Pair<S, T>> stateChange;

	public State(Function<S, Pair<S, T>> stateChange) {
		this.stateChange = stateChange;
	}

	public <U> State<S, U> bind(final Function<T, State<S, U>> f) {
		return new State<S, U>(currentState -> {
			Pair<S, T> values = stateChange.apply(currentState);
			State<S, U> newStateChange = f.apply(values.snd());
			return newStateChange.run(values.fst());
		});
	}

	public Pair<S, T> run(S initialState) {
		return stateChange.apply(initialState);
	}

	public static <S, T> State<S, T> unit(final T valueToProduce) {
		Function<S, Pair<S, T>> dummyChangeState = currentState -> new Pair<S, T>(currentState, valueToProduce);
		return new State<S, T>(dummyChangeState);
	}
}
