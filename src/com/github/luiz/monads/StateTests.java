package com.github.luiz.monads;

import java.util.function.Function;

public class StateTests {

	public static void main(String[] args) {
		// logging
		System.out.println(State.<String,Integer> unit(1)
				.bind(doubleValue())
				.bind(doubleValue())
				.run("Apply\n"));

		// simulating input
		System.out.println(State.<String, String> unit("")
				.bind(getLine())
				.bind(getLine())
				.run("Hey!\nMy name is Luiz\nWhat's yours?\nHow old are you?\n"));

		// simulating output
		System.out.println(State.<String, Void> unit(null)
				.bind(putStrLn("Hi again!"))
				.bind(putStrLn("How are you doing?"))
				.bind(putStrLn("Fine?"))
				.run("Executing: "));

		// example from QCon SP 2012 lightning talk
		System.out.println(State.<Carro,Void> unit(null)
				.bind(anda())
				.bind(andaMais())
				.bind(getQuilometragem())
				.run(new Carro()));
	}


	private static Function<String, State<String, String>> getLine() {
		return ignoredLastResult -> new State<>(currentText -> {
			int cutAt = currentText.indexOf('\n');
			String newLine = currentText.substring(0, cutAt);
			String nonConsumedInput = currentText.substring(cutAt + 1);
			return new Pair<>(nonConsumedInput, newLine);
		});
	}


	/*
	 * my state-changing function multiplies the value of the last computation
	 * by two and changes the state, which is a String, to log its application
	 */
	private static Function<Integer, State<String, Integer>> doubleValue() {
		return lastComputationValue -> {
			return new State<>(oldState -> new Pair<>(oldState + "Applied again\n", lastComputationValue * 2));
		};
	}


	private static Function<Void, State<Carro, Void>> anda() {
		return ignore -> new State<Carro, Void>(c -> new Pair<>(c.anda(), null));
	}

	private static Function<Void, State<Carro, Void>> andaMais() {
		return ignore -> new State<Carro, Void>(c -> new Pair<>(c.andaMais(), null));
	}

	private static Function<Void, State<Carro, Integer>> getQuilometragem() {
		return ignore -> new State<Carro, Integer>(c -> new Pair<>(c, c.getQuilometragem()));
	}

	private static Function<Void, State<String, Void>> putStrLn(final String str) {
		return lastIgnoredResult -> new State<String, Void>(currentOutput -> {
			String newOutputState = currentOutput + str + "\n";
			return new Pair<>(newOutputState, null);
		});
	}
}
