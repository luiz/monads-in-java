package com.github.luiz.monads;

public class State<S, T> {

	private final Function<S, Pair<S, T>> stateChange;

	public State(Function<S, Pair<S, T>> stateChange) {
		this.stateChange = stateChange;
	}

	public <U> State<S, U> bind(final Function<T, State<S, U>> f) {
		return new State<S, U>(new Function<S, Pair<S, U>>() {
			@Override
			public Pair<S, U> apply(S currentState) {
				Pair<S, T> values = stateChange.apply(currentState);
				State<S, U> newStateChange = f.apply(values.snd());
				return newStateChange.run(values.fst());
			}
		});
	}

	public Pair<S, T> run(S initialState) {
		return stateChange.apply(initialState);
	}

	public static <S, T> State<S, T> unit(final T valueToProduce) {
		Function<S, Pair<S, T>> dummyChangeState = new Function<S, Pair<S, T>>() {
			@Override
			public Pair<S, T> apply(S currentState) {
				return new Pair<S, T>(currentState, valueToProduce);
			}
		};
		return new State<S, T>(dummyChangeState);
	}
}
