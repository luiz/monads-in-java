package com.github.luiz.monads;

public class StateTests {

	public static void main(String[] args) {
		// logging
		System.out.println(State.<String,Integer> unit(1)
				.bind(doubleValue())
				.bind(doubleValue())
				.run("Apply\n"));

		// simulating input
		System.out.println(State.<String, String>unit("")
				.bind(getLine())
				.bind(getLine())
				.run("Hey!\nMy name is Luiz\nWhat's yours?\nHow old are you?\n"));

		// simulating output
		System.out.println(State.<String, Void>unit(null)
				.bind(putStrLn("Hi again!"))
				.bind(putStrLn("How are you doing?"))
				.bind(putStrLn("Fine?"))
				.run("Executing: "));

		// example from QCon SP 2012 lightning talk
		System.out.println(State.<Carro,Void>unit(null)
				.bind(anda())
				.bind(andaMais())
				.bind(getQuilometragem())
				.run(new Carro()));
	}


	private static Function<String, State<String, String>> getLine() {
		return new Function<String, State<String, String>>() {

			@Override
			public State<String, String> apply(String ignoredLastResult) {
				return new State<>(new Function<String, Pair<String, String>>() {
					@Override
					public Pair<String, String> apply(String currentText) {
						int cutAt = currentText.indexOf('\n');
						String newLine = currentText.substring(0, cutAt);
						String nonConsumedInput = currentText.substring(cutAt + 1);
						return new Pair<>(nonConsumedInput, newLine);
					}
				});
			}
		};
	}


	/*
	 * my state-changing function multiplies the value of the last computation
	 * by two and changes the state, which is a String, to log its application
	 */
	private static Function<Integer, State<String, Integer>> doubleValue() {
		return new Function<Integer, State<String, Integer>>() {

			@Override
			public State<String, Integer> apply(final Integer lastComputationValue) {

				Function<String, Pair<String, Integer>> transitionFunction = new Function<String, Pair<String, Integer>>() {
					public Pair<String, Integer> apply(String oldState) {
						return new Pair<>(oldState + "Applied again\n", lastComputationValue * 2);
					}
				};
				return new State<>(transitionFunction);
			}

		};
	}


	private static Function<Void, State<Carro, Void>> anda() {
		return new Function<Void, State<Carro, Void>>() {

			@Override
			public State<Carro, Void> apply(Void ignore) {
				return new State<Carro, Void>(new Function<Carro, Pair<Carro, Void>>() {
					@Override
					public Pair<Carro, Void> apply(Carro c) {
						return new Pair<>(c.anda(), null);
					}
				});
			}
		};
	}

	private static Function<Void, State<Carro, Void>> andaMais() {
		return new Function<Void, State<Carro, Void>>() {

			@Override
			public State<Carro, Void> apply(Void ignore) {
				return new State<Carro, Void>(new Function<Carro, Pair<Carro, Void>>() {
					@Override
					public Pair<Carro, Void> apply(Carro c) {
						return new Pair<>(c.andaMais(), null);
					}
				});
			}
		};
	}

	private static Function<Void, State<Carro, Integer>> getQuilometragem() {
		return new Function<Void, State<Carro, Integer>>() {

			@Override
			public State<Carro, Integer> apply(Void ignore) {
				return new State<Carro, Integer>(new Function<Carro, Pair<Carro, Integer>>() {
					@Override
					public Pair<Carro, Integer> apply(Carro c) {
						return new Pair<>(c, c.getQuilometragem());
					}
				});
			}
		};
	}

	private static Function<Void, State<String, Void>> putStrLn(final String str) {
		return new Function<Void, State<String, Void>>() {

			@Override
			public State<String, Void> apply(Void lastIgnoredResult) {
				return new State<String, Void>(new Function<String, Pair<String, Void>>() {
					@Override
					public Pair<String, Void> apply(String currentOutput) {
						String newOutputState = currentOutput + str + "\n";
						return new Pair<>(newOutputState, null);
					}
				});
			}
		};
	}
}
