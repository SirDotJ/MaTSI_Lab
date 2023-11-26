package org.example.lab6;

import org.example.common.ExtendedGCD;

public class RSAPrivateKey {
	private final int multiplicativeInverse; // d
	private final int modulus; // n
	public RSAPrivateKey(RSAPublicKey publicKey, int p, int q) {
		int eulerFunctionValue = (p - 1) * (q - 1);
		this.modulus = publicKey.getModulus();
		this.multiplicativeInverse = ExtendedGCD.inverse(publicKey.getPublicExponent(), eulerFunctionValue);
	}
	int getMultiplicativeInverse() {
		return multiplicativeInverse;
	}

	int getModulus() {
		return modulus;
	}

	/* WARNING: FOR DEMONSTRATION PURPOSES ONLY */
	@Override
	public String toString() {
		return ("(d = " + this.multiplicativeInverse + ", n = " + this.modulus + ")");
	}
}
