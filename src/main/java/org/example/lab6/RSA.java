package org.example.lab6;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeoutException;

import static java.lang.Math.pow;

class RSA {
    private static List<Integer> primes = new ArrayList<>(Arrays.asList(
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89,
            1847,
            8663,
            4967,
            8087,
            52583,
            62927,
            19319,
            54983,
            90647,
            73079,
            31799,
            178559,
            48023,
            269663,
            367559,
            210263,
            598439,
            1367159,
            2356919,
            6707879,
            8915639,
            21784199
    ));

    public static void main(String[] args) {
//        List<Integer> primes = new ArrayList<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89));
//        generatePrimes(primes, new PrintWriter(System.out));
//        System.out.println();
//        primes.forEach(System.out::println);
//        Map<Integer, Integer> keys = findValidKeys(primes);
//        keys.forEach((key, value) -> {
//            System.out.println(key + "; " + value + ": " + (primesToEachOther(key, value) ? "Primes to each other" : "Not primes to each other"));
//        });

        /* Выбранные ключи и чила n, m */
        int p = 89; // Prime number
        int q = 71; // Prime number
        int n = p * q;
        int m = (p - 1) * (q - 1);


        /* Создание числа e */
//        int m = 6160;
//        try {
//            int e = randomPrimeToNumber(m);
//            System.out.println(e);
//        } catch (TimeoutException e) {
//            System.out.println(e);
//        }

        /* Создание числа d */
//        int m = 6160;
//        int e = 3167;
//        try {
//            int d = multiplicativeOpposite(e, m);
//            System.out.println(d);
//        } catch (TimeoutException exception) {
//            exception.printStackTrace();
//        }

        /* Зашифровка */
//        int e = 3167;
//        int n = 6319;
//        List<Integer> message = new ArrayList<>(Arrays.asList(13, 16, 4));
//        List<Integer> encryptedMessage = new ArrayList<>();
//        for (int i = 0; i < message.size(); i++) {
//            encryptedMessage.add(calcC(message.get(i), e, n));
//            System.out.println(encryptedMessage.get(i));
//        }

        /* Расшифровка */
//        int d = 5983;
//        int n = 6319;
//        List<Integer> encryptedMessage = new ArrayList<>(Arrays.asList(4231, 5201, 1669));
//        List<Integer> decryptedMessage = new ArrayList<>();
//        for (int i = 0; i < encryptedMessage.size(); i++) {
//            decryptedMessage.add(calcC(encryptedMessage.get(i), d, n));
//            System.out.println(decryptedMessage.get(i));
//        }
    }

    public static int calcC (int M, int e, int n) {
        BigInteger power = new BigInteger(String.valueOf(M));
        BigInteger bigPower = power.pow(e);
        BigInteger bigN = new BigInteger(String.valueOf(n));
        return Integer.parseInt(bigPower.mod(bigN).toString());
    }

    public static int calcD (int C, int d, int n) {
        return calcC(C, d, n); // та же самая формула
    }

    public static int multiplicativeOpposite(int number, int module) throws TimeoutException {
        for (int foundNumber = 0; foundNumber != Integer.MAX_VALUE; foundNumber++) {
            if (foundNumber * number % module == 1)
                return foundNumber;
        }
        throw new TimeoutException("No number found");
    }

    public static int randomPrimeToNumber(int number) throws TimeoutException {
        int counter = 0;
        do {
            int randomNumber = (int) (Math.random() * number + 1);
            if (primesToEachOther(randomNumber, number))
                return randomNumber;
        } while (++counter != Integer.MAX_VALUE);
        throw new TimeoutException("No random number found");
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

    public static Map<Integer, Integer> findValidKeys(List<Integer> primes) {
        Map<Integer, Integer> foundKeys = new HashMap<>();
        for (int i = 0; i < primes.size(); i++) {
            for (int j = 0; j < primes.subList(i, primes.size() - 1).size(); j++) {
                if (pow(primes.get(i), primes.get(j)) % primes.get(j) == primes.get(i))
                        foundKeys.put(primes.get(i), primes.get(j));
            }
        }
        return foundKeys;
    }

    /** Генерация простых чисел путём многократного перемножения входного набора заведомо простых.
     * На каждой итерации одно входное простое перемножается с другим, затем результат умножается на 2,
     * после чего к результату прибавляется единица:
     * probablyPrime=prime1*prime2*2+1
     * Каждое потенциально простое далее проверяется на псевдопростоту в функции addIfPrime.
     * Если псевдопростота исключена, то потенциально простое проверяется на простоту при помощи аналога
     * теста Ферма, являющегося детерминированным для любых чисел, не являющихся псевдопростыми по основанию 2. */
    public static void generatePrimes(List<Integer> primes, PrintWriter pw)
    {
        // Список простых чисел с остатком при деление по модулю 3 = 1.
        List<BigInteger> mod3_1 = new ArrayList<>();
        // Список чисел признанных простыми функцией addIfPrime()
        List<BigInteger> l = new ArrayList<BigInteger>();
        // Экземпляры больших чисел со значением 3 и 2.
        BigInteger three = BigInteger.valueOf(3), two = BigInteger.valueOf(2);
        // Цикл обработки простых чисел, данных в виде входящего параметра в процедуру.
        // В цикле для каждого входного простого вычисляется его произведение со всеми остальными простыми.
        // Если результат перемножения не равен единице по модулю 3, то такие числа игнорируются из-за
        // порождения при последующих пермножениях чисел, кратных трём.
        for (int k = 0; k < primes.size() - 1; k++)
        {
            // Рассматриваемое простое число (а)
            BigInteger seed = BigInteger.valueOf(primes.get(k));
            // Удвоенное простое число (2а)
            BigInteger s2 = seed.shiftLeft(1);
            // Остаток при делении `a` на 3
            BigInteger r0 = seed.remainder(three);
            // Проверка на остаток = 1
            if (r0.intValue() == 1) mod3_1.add(seed);
            // Цикл по тем простым числам, с которыми данное число пока что не перемножалось
            for (int i = k + 1; i < primes.size(); i++)
            {
                BigInteger p = BigInteger.valueOf(primes.get(i)); // Перевод типа int в тип BigInteger
                BigInteger r = p.remainder(three); // Остаток от деления p на 3
                // Если остатки от деления очередного простого числа на три и ранее выбранного так же простого
                // равны друг другу, то такую пару игнорируем из-за обязательной делимости результата на 3
                if (r.intValue() == r0.intValue()) continue; // divisible by 3
                else addIfPrime(p, seed, s2, two, l); // Если делимости на 3 нет, то проверяем на простоту
            }
        }
        // Фиксируем ссылку на полученные выше результаты перемножений и проверок простоты в перменной ps
        // (ps - сокращение от primes)
        List<BigInteger> ps = l;
        // В этом цикле каждое ранее найденное простое перемножается с ранее отобранными простыми,
        // дающими по модулю 3 единицу. Результат перемножения проверяется на простоту функцией addIfPrime.
        //
        do
        {
            System.out.println("found " + l.size() + ", bits=" + l.get(0).bitLength());
            l = new ArrayList<BigInteger>();
            for (int k = 0; k < ps.size(); k++)
            {
                BigInteger seed = ps.get(k);
                BigInteger s2 = seed.shiftLeft(1);
                // Проходим по списку равных единице по модулю тройки чисел и перемножаем их на
                // ранее полученные простые результаты аналогичных перемножений
                for (int i = 0; i < mod3_1.size(); i++)
                    addIfPrime(mod3_1.get(i), seed, s2, two, l);
                // Здесь проверяем разрядность полученных простых чисел. Если разрядность превышает порог в
                // 700, 800, 900, то меняем максимально допустимое значение размера списка получаемых простых
                // с целью ограничения мощности генерации. Если генерацию не ограничивать, то количество промежуточных
                // простых, меньших чем требуемые нам числа криптографических порядков, будет очень большим.
                int n = 100000; // change following to adjust for required bit count
                if (l.size() > 0)
                    if (l.get(0).bitLength() < 700) n = 10;
                    else if (l.get(0).bitLength() < 800) n = 20;
                    else if (l.get(0).bitLength() < 900) n = 40;
                if (l.size() > n) break; // Если количество полученных простых больше максимально допустимого, то выходим из данного цикла
            }
            ps = l; // Фиксируем ссылку на полученные выше результаты перемножений и проверок простоты в перменной ps
            // Записываем все полученные простые в поток, полученный на входе процедуры
            for (int i = 0; i < l.size(); i++)
                pw.println(l.get(i));
            pw.flush(); // дописываем буфер потока, что бы зафиксировать все полученные результаты
        }
        while (l.size() > 0);
        System.out.println("Done");
    }
    /** Вычисляем новое простое, перемножая два известных простых, затем удваивая произведение и прибавляя единицу.
     * Полученое потенциально простое число проверяется на потенциальную псевдопростоту согласно критериям из статьи.
     * Если число не является псевдопростым, то далее проводится стандартный вероятностный тест простоты,
     * который в данном случае является детерминированным в следствии устранения возможности появления
     * псевдопростых чисел предыдущей фильтрацией. */
    private static void addIfPrime(BigInteger a, BigInteger b, BigInteger b2, BigInteger two, List<BigInteger> l)
    {
        // a2=2*a; fp=a*b*2; n=a*b*2+1;
        BigInteger a2=a.shiftLeft(1), fp=b.multiply(a2), n=fp.add(BigInteger.ONE);
        BigInteger r=null;
        if (a2.compareTo(b)<0) r=two.modPow(a2,n); // 2a<b
        else if (a.compareTo(b2)<0) r=two.modPow(a,n); // a<2b
        if (r!=null && r.compareTo(BigInteger.ONE)==0) return;
        r=null;
        if (b2.compareTo(a)<0) r=two.modPow(b2,n); // 2b<a
        else if (b.compareTo(a2)<0) r=two.modPow(b,n); // b<2a
        if (r!=null && r.compareTo(BigInteger.ONE)==0) return;
        // Детерминированная проверка простоты (для случая, исключающего наличие псевдопростых числе)
        // при помощи вычисления остатка по формуле:
        // r=2^(fp/2) mod n
        r=two.modPow(fp.shiftRight(1),n);
        if (r.compareTo(BigInteger.ONE)!=0) return;
        l.add(n);
    }
}
