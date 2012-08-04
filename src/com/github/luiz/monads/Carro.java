package com.github.luiz.monads;

public class Carro {
	private final int quilometragem;

	public Carro() {
		this(0);
	}

	private Carro(int quilometragem) {
		this.quilometragem = quilometragem;
	}

	public Carro anda() {
		return new Carro(quilometragem + 100);
	}

	public Carro andaMais() {
		return new Carro(quilometragem + 1000);
	}

	public int getQuilometragem() {
		return quilometragem;
	}
}