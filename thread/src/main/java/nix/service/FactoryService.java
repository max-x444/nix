package nix.service;

import lombok.NonNull;
import lombok.SneakyThrows;
import nix.model.Factory;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FactoryService {
    private static final Random RANDOM = new Random();
    private static final CountDownLatch LATCH_ALL_THREADS;
    private static final CountDownLatch LATCH_CONSTRUCTION_DETAILS;
    private static final CountDownLatch LATCH_CHIP;
    private static FactoryService instance;
    private final Factory factory;

    static {
        final int numberOfThreads = 5;
        final int numberOfThreadsConstructionDetails = 2;
        final int numberOfThreadsChip = 3;
        LATCH_ALL_THREADS = new CountDownLatch(numberOfThreads);
        LATCH_CONSTRUCTION_DETAILS = new CountDownLatch(numberOfThreadsConstructionDetails);
        LATCH_CHIP = new CountDownLatch(numberOfThreadsChip);
    }

    private FactoryService() {
        factory = new Factory();
    }

    public static FactoryService getInstance() {
        if (instance == null) {
            instance = new FactoryService();
        }
        return instance;
    }

    @SneakyThrows
    public Factory startFactory() {
        fuelExtraction();
        assemblyOfTheBasicConstructionOfDetails("Second robot");
        assemblyOfTheBasicConstructionOfDetails("Third robot");

        LATCH_CONSTRUCTION_DETAILS.await();
        chipProgramming();

        LATCH_CHIP.await();
        shapingDetailsIntoFinishedForm();

        LATCH_ALL_THREADS.await();
        return factory;
    }

    /**
     * Robot 1 - does background work - extracts fuel for the factory
     * - In one iteration, the robot can extract 500-1000 gallons of fuel,
     * after which it spends 3 seconds to transport.
     */
    private void fuelExtraction() {
        final AtomicInteger extractedFuelByRobot = new AtomicInteger();
        final ScheduledExecutorService firstRobot = Executors.newSingleThreadScheduledExecutor();
        firstRobot.scheduleWithFixedDelay(() -> {
            Thread.currentThread().setName("First robot");
            if (factory.getFinishedDetail().get() != 100) {
                extractedFuelByRobot.set(RANDOM.nextInt(500, 1000));
                factory.getFuel().addAndGet(extractedFuelByRobot.get());
                factory.getTotalFuelProduced().addAndGet(extractedFuelByRobot.get());
                System.out.println(Thread.currentThread().getName() + " produced " + extractedFuelByRobot + " gallons of fuel");
            } else {
                firstRobot.shutdownNow();
                LATCH_ALL_THREADS.countDown();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    /**
     * Robot 2 and Robot 3 - assemble the basic design of the part
     * - In one iteration, they can complete 10-20 items of work
     * - Then there is a reboot for 2 seconds
     * - Work is considered completed when 100+ points are reached
     */
    private void assemblyOfTheBasicConstructionOfDetails(@NonNull final String threadName) {
        final AtomicInteger points = new AtomicInteger();
        final ScheduledExecutorService secondRobot = Executors.newSingleThreadScheduledExecutor();
        secondRobot.scheduleWithFixedDelay(() -> {
            Thread.currentThread().setName(threadName);
            points.set(RANDOM.nextInt(5, 10));
            factory.getConstructionDetails().addAndGet(points.get());
            System.out.println(Thread.currentThread().getName() + " completed " + points + " points");
            if (factory.getConstructionDetails().get() >= 100) {
                secondRobot.shutdownNow();
                LATCH_CONSTRUCTION_DETAILS.countDown();
                LATCH_CHIP.countDown();
                LATCH_ALL_THREADS.countDown();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Robot 4 - starts its work after the completion of Robots 2 and 3
     * - In one iteration, the robot programs microcircuits in the range 25-35 points
     * - Reboot 1 second
     * - With a probability of 30%, the robot can break the chip,
     * and you have to start over the whole process
     * - Work is considered completed when 100+ points are reached
     */
    private void chipProgramming() {
        final AtomicInteger points = new AtomicInteger();
        final AtomicInteger breakageProbability = new AtomicInteger();
        final ScheduledExecutorService fourthRobot = Executors.newSingleThreadScheduledExecutor();
        fourthRobot.scheduleWithFixedDelay(() -> {
            Thread.currentThread().setName("Fourth robot");
            breakageProbability.set(RANDOM.nextInt(1, 10));
            if (1 <= breakageProbability.get() && breakageProbability.get() <= 3) {
                System.out.println("The Fourth robot broke the chip");
                factory.getChip().set(0);
            } else {
                points.set(RANDOM.nextInt(25, 35));
                factory.getChip().addAndGet(points.get());
                System.out.println(Thread.currentThread().getName() + " completed " + points + " points of the programming chip");
            }
            if (factory.getChip().get() >= 100) {
                fourthRobot.shutdownNow();
                LATCH_CHIP.countDown();
                LATCH_ALL_THREADS.countDown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Robot 5 - forms the part into a finished form, but for this he needs
     * fuel mined by robot 1
     * - In one iteration, the robot advances by 10 points
     * - Reboot 1 second
     * - One iteration requires 350-700 fuel
     * - If there is no fuel, then the robot falls asleep until the reserves are replenished
     * - Work is considered completed when 100+ points are reached
     */
    private void shapingDetailsIntoFinishedForm() {
        final int points = 10;
        final AtomicInteger requiredFuel = new AtomicInteger();
        final ScheduledExecutorService fifthRobot = Executors.newSingleThreadScheduledExecutor();
        fifthRobot.scheduleWithFixedDelay(() -> {
            Thread.currentThread().setName("Fifth robot");
            requiredFuel.set(RANDOM.nextInt(350, 700));
            if (requiredFuel.get() <= factory.getFuel().get()) {
                factory.getFinishedDetail().addAndGet(points);
                factory.getFuel().set(factory.getFuel().get() - requiredFuel.get());
                factory.getTotalFuelUsed().addAndGet(requiredFuel.get());
                System.out.println(Thread.currentThread().getName() + " completed " + points + " points");
            }
            if (factory.getFinishedDetail().get() == 100) {
                fifthRobot.shutdownNow();
                LATCH_ALL_THREADS.countDown();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}