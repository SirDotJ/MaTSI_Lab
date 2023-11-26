package org.example.common;

public class PrimeMath {
	public static boolean primesToEachOther(int prime1, int prime2) {
        return ExtendedGCD.gcd(prime1, prime2) == 1;
    }
}
