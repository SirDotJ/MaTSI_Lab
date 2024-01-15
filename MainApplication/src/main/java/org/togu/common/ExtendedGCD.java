package org.togu.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Реализация расширенного алгоритма Евклида:
* - gcd - НОД;
* - extended_gcd - расширенный НОД;
* - inverse - мультипликативно обратное к числу по модулю
*  */
public class ExtendedGCD {
	public static int gcd(int first, int second) {
		int gcd = -1;
        while (true) {
            int higher = Math.max(first, second);
            int lower = Math.min(first, second);
            int remainder = higher % lower;
            if (remainder <= 0)
                break;
            gcd = remainder;
            first = remainder;
            second = lower;
        }
        return gcd;
	}

	public static List<Integer> extended_gcd(int a, int b) {
		int old_r = a;
		int r = b;
		int old_s = 1;
		int s = 0;
		int old_t = 0;
		int t = 1;

		while (r != 0) {
			int quotient = old_r / r;

			int tempOldR = old_r;
			old_r = r;
			r = tempOldR - quotient * r;

			int tempOldS = old_s;
			old_s = s;
			s = tempOldS - quotient * s;

			int tempOldT = old_t;
			old_t = t;
			t = tempOldT - quotient * t;
		}
		// Coefficient1, Coefficient2, GCD
		return new ArrayList<>(Arrays.asList(old_s, old_t, old_r));
	}

	public static int inverse(int a, int n) {
		int t = 0;
		int newT = 1;
		int r = n;
		int newR = a;

		while (newR != 0) {
			int quotient = r / newR;

			int tempT = t;
			t = newT;
			newT = tempT - quotient * newT;

			int tempR = r;
			r = newR;
			newR = tempR - quotient * newR;
		}

		if (r > 1)
			throw new IllegalArgumentException("a is not invertible");
		if (t < 0)
			t = t + n;

		return t;
	}
}
