package org.example.lab6;

import org.example.common.PrimeMath;

import java.util.concurrent.TimeoutException;

public class RSAPublicKey {
	private final int publicExponent; // e
	private final int modulus; // n
	public RSAPublicKey(int prime1, int prime2) throws IllegalArgumentException {
		int modulus = prime1 * prime2;
		int eulerFunctionValue = (prime1 - 1) * (prime2 - 1);
		this.modulus = modulus;
		try {
			this.publicExponent = generatePublicExponent(eulerFunctionValue);
		} catch (TimeoutException e) {
			throw new IllegalArgumentException("Provided values do not allow for publicExponent to be made!");
		}
	}
	private static int generatePublicExponent(int eulerFunctionValue) throws TimeoutException {
		int counter = 0;
        do {
            int randomNumber = (int) (Math.random() * (eulerFunctionValue - 1) + 2); // 1 < exponent < euler
            if (PrimeMath.primesToEachOther(randomNumber, eulerFunctionValue))
                return randomNumber;
        } while (++counter != Integer.MAX_VALUE);
        throw new TimeoutException("No valid public exponent could be made in Integer.MAX_VALUE tries");
	}
	int getPublicExponent() {
		return publicExponent;
	}
	int getModulus() {
		return modulus;
	}

	/* WARNING: FOR DEMONSTRATION PURPOSES ONLY */
	@Override
	public String toString() {
		return ("(e = " + this.publicExponent + ", n = " + this.modulus + ")");
	}
}
