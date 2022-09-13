package nix;

import nix.util.MyThreadUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final List<Integer> numbers = List.of(0, 1, -5, 2, 4, 3, 5, 11, 33, 100, 7, 13, 25, 39, 11, 17, 150, 201);
        MyThreadUtil.getFiftyThreads();
        MyThreadUtil.getCountOfPrimeNumbers(numbers);
    }
}