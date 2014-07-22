package com.github.luiz.monads;

public class Pair<T1, T2> {
	private final T1 v1;
	private final T2 v2;

	public static <T1, T2> Pair<T1, T2> p(T1 v1, T2 v2) {
		return new Pair<T1, T2>(v1, v2);
	}

	public Pair(T1 v1, T2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	public T1 fst() {
		return v1;
	}

	public T2 snd() {
		return v2;
	}

	@Override
	public String toString() {
		return "(" + v1 + ", " + v2 + ")";
	}
}
