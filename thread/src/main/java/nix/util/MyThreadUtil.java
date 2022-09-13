package nix.util;

import lombok.NonNull;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class MyThreadUtil {
    private MyThreadUtil() {
    }

    @SneakyThrows
    public static int getCountOfPrimeNumbers(@NonNull final List<Integer> numbers) {
        final AtomicInteger countOfPrimeNumbers = new AtomicInteger();
        final Thread firstThread = new Thread(() ->
                countOfPrimeNumbers.addAndGet(calculateCountOfPrimeNumbers(numbers.subList(0, numbers.size() / 2))));
        final Thread secondThread = new Thread(() ->
                countOfPrimeNumbers.addAndGet(calculateCountOfPrimeNumbers(numbers.subList(numbers.size() / 2, numbers.size()))));
        firstThread.start();
        secondThread.start();
        firstThread.join();
        secondThread.join();
        System.out.println("Total count of prime numbers: " + countOfPrimeNumbers.get());
        return countOfPrimeNumbers.get();
    }

    @SneakyThrows
    public static void getFiftyThreads() {
        final Thread[] threads = new Thread[50];
        for (int i = 49; i != -1; i--) {
            threads[i] = new Thread(() -> System.out.println("Hello from " + Thread.currentThread().getName()));
        }
        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }
    }

    private static int calculateCountOfPrimeNumbers(@NonNull final List<Integer> primeNumbers) {
        int count = 0;
        for (Integer primeNumber : primeNumbers) {
            if (isPrime(primeNumber)) {
                count++;
            }
        }
        System.out.println(Thread.currentThread().getName() + " found " + count + " primes numbers");
        return count;
    }

    private static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        } else if (n == 2) {
            return true;
        } else if (n % 2 == 0) {
            return false;
        }
        for (int i = 3; i <= Math.sqrt(n); i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}