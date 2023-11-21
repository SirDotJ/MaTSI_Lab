package org.example.lab6;

import java.math.BigInteger;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static java.lang.Math.pow;

public class ElGamal {

    /* X_i Y_i */
    public static List<Map<Integer, Integer>> keys = new ArrayList<>(Arrays.asList(
            new HashMap<>(Map.of(69, 133)),
            new HashMap<>(Map.of(52, 17))
    ));

    /* Все пишут "ШОК" */
    public static List<List<Integer>> messages = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList(
                    15, 3, 3
            )),
            new ArrayList<>(Arrays.asList(
                    15, 3, 3
            ))
    ));

    public static void main(String[] args) {
        int P = 269;
        int A = 199;

        /* Общие ключи P и A и их проверка */
        System.out.println("P = " + P);
        System.out.println("A = " + A);


        /* P и A - взаимно простые */
        System.out.println(primesToEachOther(P, A) ? "P и A - Взаимно простые" : "P и A - Не взаимно простые!");
        /* A^P mod P = A */
        System.out.println(((int) (pow(A, P) % P) == A) ? "Различные степени A - числа по модулю P" : "Ошибка, A - не различные степени А по модулю P");

        /* Генерируем ключи для абонентов сети */
//        int N = 5; // Количество абонентов сети
//        List<Map<Integer, Integer>> keys = new ArrayList<>(); /* (X_i; Y_i) */
//        for (int i = 0; i < N; i++) {
//            int Xi = calcX(P);
//            int Yi = calcY(P, A, Xi);
//            keys.add(new HashMap<>(Map.of(Xi, Yi)));
//        }
//        keys.forEach((map) -> {
//            int X = (int) map.keySet().toArray()[0];
//            int Y = map.get(X);
//            System.out.println("X = " + X + "; Y = " + Y);
//        });

        /* Шифровка */
        for (int i = 0; i < messages.size(); i++) { /* Отправитель */
            List<Integer> message = messages.get(i);
            System.out.print("User №" + i + ". Message: ");
            message.forEach((number) -> System.out.print(number + " "));
            System.out.println();

            /* Публичные ключи */
            int openP = P;

            /* Ключи отправителя */
            int XSender = (int) keys.get(i).keySet().toArray()[0];
            int YSender = keys.get(i).get(XSender);

            for (int j = 0; j < keys.size(); j++) { /* Получатель */
                if (i == j)
                    continue;

                /* Ключи получателя */
                Map<Integer, Integer> keyReceiver = keys.get(j);
                int XReceiver = (int) keyReceiver.keySet().toArray()[0];
                int YReceiver = keyReceiver.get(XReceiver);

                List<Integer> encryptedMessage = new ArrayList<>();
                for (int l = 0; l < message.size(); l++) {
                    int Mi = message.get(l);
                    encryptedMessage.add(encrypt(Mi, openP, YReceiver, XSender));
                }

                System.out.println("User №" + j + ":\n\tReceived message:");
                for (int l = 0; l < encryptedMessage.size(); l++) {
                    int number = encryptedMessage.get(l);
                    System.out.println("\t№" + l + ": " + number);
                }
                System.out.println("\tDecrypted message:");
                System.out.print("\t");
                for (int l = 0; l < encryptedMessage.size(); l++) {
                    int e = encryptedMessage.get(l);
                    int decrypted = decrypt(e, openP, YSender, XReceiver);
                    System.out.print(decrypted + " ");
                }
            }
        }
    }

    public static int encrypt(int Mi, int P, int YReceiver, int XSender) {
        /* (Mi * Yr^Xs) mod P */
        BigInteger bigMi = new BigInteger(String.valueOf(Mi));
        BigInteger bigP = new BigInteger(String.valueOf(P));
        BigInteger bigYReceiver = new BigInteger(String.valueOf(YReceiver));

        BigInteger power = bigYReceiver.pow(XSender);
        BigInteger multiplication = bigMi.multiply(power);
        BigInteger remainder = multiplication.mod(bigP);
        return remainder.intValue();
    }

    public static int decrypt(int e, int P, int YSender, int XReceiver) {
        /* M(i) = e * r^{P - 1 - Xi} mod P */
        BigInteger bigE = new BigInteger(String.valueOf(e));
        BigInteger bigP = new BigInteger(String.valueOf(P));
        BigInteger bigYSender = new BigInteger(String.valueOf(YSender));

        BigInteger power = bigYSender.pow(P - 1 - XReceiver);
        BigInteger multiplication = power.multiply(bigE);
        BigInteger remainder = multiplication.mod(bigP);
        return remainder.intValue();
    }

    public static int calcX(int P) {
        /* X_i \in (1; P - 1) */
        return (int) (Math.random() * (P - 1) + 1);
    }

    public static int calcY(int P, int A, int Xi) {
        /* Y_i = A^Xi mod P */
        return (int) (pow(A, Xi) % P);
    }

    public static boolean primesToEachOther(int first, int second) {
        return lowestCommonDenominator(first, second) == 1;
    }

    public static int lowestCommonDenominator(int first, int second) {
        int lowestCommonDenominator = -1;
        while (true) {
            int higher = Math.max(first, second);
            int lower = Math.min(first, second);
            int remainder = higher % lower;
            if (remainder <= 0)
                break;
            lowestCommonDenominator = remainder;
            first = remainder;
            second = lower;
        }
        return lowestCommonDenominator;
    }
}
