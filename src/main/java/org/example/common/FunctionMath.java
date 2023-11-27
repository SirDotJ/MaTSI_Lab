package org.example.common;

import org.example.lab8.Trithemius;

import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class FunctionMath {
	public static Function<Integer, Integer> generateRandomValidTrithemiusFunction(Alphabet alphabet, int power) throws TimeoutException {
		Function<Integer, Integer> function;
		boolean success = false;
		int counter = 0;
		do {
			int coefficient1 = (int) (Math.random() * power - power);
			int coefficient2 = (int) (Math.random() * power - power);
			int coefficient3 = (int) (Math.random() * power - power);
			function = cypherFunctionGenerator(coefficient1, coefficient2, coefficient3);
			try {
				Trithemius encryptor = new Trithemius(alphabet, function);
			} catch (IllegalArgumentException e) {
				continue;
			}
			success = true;
		} while (!success && counter++ != Integer.MAX_VALUE);
		if (!success)
			throw new TimeoutException("No valid formula could be found in time");

		return function;
	}
	public static Function<Integer, Integer> cypherFunctionGenerator(int coefficient1, int coefficient2, int coefficient3) {
		return integer -> coefficient1 * (int) Math.pow(integer, 2) + coefficient2 * integer + coefficient3;
	}
}
