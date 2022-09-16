package nix.model;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@ToString
public class Factory {
    private final AtomicInteger constructionDetails = new AtomicInteger();
    private final AtomicInteger finishedDetail = new AtomicInteger();
    private final AtomicInteger chip = new AtomicInteger();
    private final AtomicInteger fuel = new AtomicInteger();
    private final AtomicInteger totalFuelProduced = new AtomicInteger();
    private final AtomicInteger totalFuelUsed = new AtomicInteger();
}