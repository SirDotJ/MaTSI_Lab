package org.example.lab7;

import org.example.common.ExtendedGCD;

import java.util.List;

public class RabinPrivateKey {
	private final int prime1;
	private final int prime2;
	private final int bezoutCoefficient1;
	private final int bezoutCoefficient2;
	RabinPrivateKey(int prime1, int prime2) {
		this.prime1 = prime1;
		this.prime2 = prime2;
		List<Integer> primeCharacteristics = ExtendedGCD.extended_gcd(prime1, prime2);
		this.bezoutCoefficient1 = primeCharacteristics.get(0);
		this.bezoutCoefficient2 = primeCharacteristics.get(1);
	}

	int getPrime1() {
		return prime1;
	}
	int getPrime2() {
		return prime2;
	}
	int getBezoutCoefficient1() {
		return bezoutCoefficient1;
	}
	int getBezoutCoefficient2() {
		return bezoutCoefficient2;
	}
}
