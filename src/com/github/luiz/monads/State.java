package com.github.luiz.monads;

public class State<S, T> implements Monad<State, T> {

	private final Function<S, Pair<S, T>> stateChange;

	private State(Function<S, Pair<S, T>> stateChange) {
		this.stateChange = stateChange;
	}

	@Override
	public <U> State<S, U> bind(final Function<T, Monad<? extends State, U>> f) {
		return new State<S, U>(new Function<S, Pair<S, U>>() {
			@Override
			public Pair<S, U> apply(S currentState) {
				Pair<S, T> values = stateChange.apply(currentState);
				State<S, U> newStateChange = (State<S, U>) f.apply(values.snd());
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

	public static void main(String[] args) {
		Function<Integer, Monad<? extends State, Integer>> doubleValue = new Function<Integer, Monad<? extends State, Integer>>() {

			@Override
			public Monad<State, Integer> apply(final Integer val) {
				return new State<String, Integer>(new Function<String, Pair<String, Integer>>() {

					@Override
					public Pair<String, Integer> apply(String oldState) {
						return new Pair<String, Integer>(oldState + "Applied again\n", val * 2);
					}
				});
			}

		};

		System.out.println(unit(1).bind(doubleValue).bind(doubleValue).run("Apply\n"));

		Function<String, Monad<? extends State, String>> getLine = new Function<String, Monad<? extends State, String>>() {

			@Override
			public Monad<State, String> apply(String ignore) {
				return new State<String, String>(new Function<String, Pair<String, String>>() {
					@Override
					public Pair<String, String> apply(String currentText) {
						int cutAt = currentText.indexOf('\n');
						return new Pair<>(currentText.substring(cutAt + 1), currentText.substring(0, cutAt));
					}
				});
			}
		};

		System.out.println(unit("").bind(getLine).bind(getLine).run("Hey!\nMy name is Luiz\nWhat's yours?\nHow old are you?\n"));

		System.out.println(unit("").bind(putStrLn("Hi again!")).bind(putStrLn("How are you doing?")).bind(putStrLn("Fine?")).run("Executing: "));

		/*
		 * The following won't compile. Can't bind two different monads. Only works because of the ugly generic Self in Monad interface
		 */
		// Function<String, Monad<? extends Maybe, String>> capitalize = new Function<String, Monad<? extends Maybe, String>>() {
		//     @Override
		//     public Monad<? extends Maybe, String> apply(String str) {
		//         return Maybe.some(str.toUpperCase());
		//     }
		// };
		//
		// System.out.println(unit("").bind(getLine).bind(capitalize).run("Hey!\nMy name is Luiz\nWhat's yours?\nHow old are you?\n"));
	}

	private static Function<String, Monad<? extends State, String>> putStrLn(final String str) {
		return new Function<String, Monad<? extends State,String>>() {

			@Override
			public State<String, String> apply(final String ignore) {
				return new State<String, String>(new Function<String, Pair<String, String>>() {
					@Override
					public Pair<String, String> apply(String currentOutput) {
						return new Pair<>(currentOutput + str + "\n", "");
					}
				});
			}
		};
	}

}
