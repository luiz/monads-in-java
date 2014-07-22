package com.github.luiz.monads;

import static com.github.luiz.monads.Pair.p;

import java.util.function.Function;

public class State<S, T> implements Monad<State, T> {

	private final Function<S, Pair<S, T>> stateChange;

	public State(Function<S, Pair<S, T>> stateChange) {
		this.stateChange = stateChange;
	}

	@Override
	public <U extends Monad<? extends State, ?>> U bind(final Function<T, U> f) {
		Function<S, Pair<S, U>> fn = currentState -> {
			Pair<S, T> values = stateChange.apply(currentState);
			State<S, U> newStateChange = (State<S, U>) f.apply(values.snd());
			return newStateChange.run(values.fst());
		};
		return (U) new State<S, U>(fn);
	}

	public Pair<S, T> run(S initialState) {
		return stateChange.apply(initialState);
	}

	public static <S, T> State<S, T> unit(T valueToProduce) {
		return new State<S, T>(currentState -> p(currentState, valueToProduce));
	}
}
