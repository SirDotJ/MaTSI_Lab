package org.example.common;

import org.example.lab8.Trithemius;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class FunctionMath {
	public static Function<Integer, Integer> generateRandomValidTrithemiusFunction(Alphabet alphabet, int power) throws TimeoutException {
		int counter = 0;
		do {
			List<Integer> coefficients = randomCoefficients(3, power);
			if (checkCoefficientValidityForTrithemius(alphabet, coefficients))
				return cypherFunctionGenerator(coefficients.get(0), coefficients.get(1), coefficients.get(2));
		} while (counter++ != Integer.MAX_VALUE);
			throw new TimeoutException("No valid formula could be found in time");
	}

	public static boolean checkCoefficientValidityForTrithemius(Alphabet alphabet, List<Integer> coefficients) {
		try {
			Trithemius encryptor = new Trithemius(
					alphabet,
					cypherFunctionGenerator(
							coefficients.get(0),
							coefficients.get(1),
							coefficients.get(2)
					)
			);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	public static List<Integer> randomCoefficients(int number, int power) {
		List<Integer> coefficients = new ArrayList<>();

		for (int i = 0; i < number; i++)
			coefficients.add((int) (Math.random() * power - power));

		return coefficients;
	}

	public static Function<Integer, Integer> cypherFunctionGenerator(int coefficient1, int coefficient2, int coefficient3) {
		return integer -> coefficient1 * (int) Math.pow(integer, 2) + coefficient2 * integer + coefficient3;
	}
}
