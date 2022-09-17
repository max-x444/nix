package nix;

import nix.service.FactoryService;

public class Main {
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        final FactoryService factoryService = FactoryService.getInstance();
        System.out.println(factoryService.startFactory());
        final long finish = System.currentTimeMillis();
        System.out.println("Total execution time: " + (finish - start) + "ms");
    }
}