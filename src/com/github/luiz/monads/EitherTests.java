package com.github.luiz.monads;

import static com.github.luiz.monads.Either.left;
import static com.github.luiz.monads.Either.right;
import static com.github.luiz.monads.Maybe.none;
import static com.github.luiz.monads.Maybe.some;

import java.util.function.Function;
import java.util.regex.Pattern;

public class EitherTests {
	public static void main(String[] args) {
		Either<Exception, Integer> finalResult = right("2a").bind(parseInt).bind(sum(2));
		System.out.println(finalResult);
	}

	static Function<String, Either<Exception, Integer>> parseInt = (value) -> {
		Pattern number = Pattern.compile("\\d+");
		if (number.matcher(value).matches()) {
			return right(Integer.parseInt(value));
		}
		return left(new IllegalArgumentException(value + " is not an integer"));
	};

	static Function<Integer, Either<Exception, Integer>> sum(int factor) {
		return (value) -> right(value + factor);
	}

	static Function<String, Maybe<Integer>> weakerParseInt = (value) -> {
		Pattern number = Pattern.compile("\\d+");
		if (number.matcher(value).matches()) {
			return some(Integer.parseInt(value));
		}
		return none();
	};
}