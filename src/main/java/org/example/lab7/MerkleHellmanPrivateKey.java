package org.example.lab7;

import org.example.common.ExtendedGCD;
import org.example.common.PrimeMath;

import java.util.Collections;
import java.util.List;

public class MerkleHellmanPrivateKey {
	private final List<Integer> W;
	private final int q;
	private final int r;
	private final int inverseR;
	public MerkleHellmanPrivateKey(List<Integer> W) {
		this.W = Collections.unmodifiableList(W);
		this.q = generateRandomQ(this.W);
		this.r = generateRandomR(this.q);
		this.inverseR = ExtendedGCD.inverse(this.r, this.q);
	}

	private static int generateRandomR(int q) {
		int foundR;
		do {
			foundR = PrimeMath.getRandomPrime();
		} while (!PrimeMath.primesToEachOther(foundR, q));
		return foundR;
	}
	private static int generateRandomQ(List<Integer> W) {
		int floor = listSum(W);
		int foundQ;
		do {
			foundQ = PrimeMath.getRandomPrime();
		} while (foundQ <= floor);
		return foundQ;
	}
	private static int listSum(List<Integer> list) {
		int sum = 0;
		for (int value : list)
			sum += value;
		return sum;
	}

	List<Integer> getW() {
		return W;
	}

	int getQ() {
		return q;
	}

	int getR() {
		return r;
	}

	int getInverseR() {
		return inverseR;
	}
}
