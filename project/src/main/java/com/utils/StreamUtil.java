package com.utils;

import com.model.Vehicle;
import com.model.VehicleType;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StreamUtil<T extends Vehicle> {
    public List<T> filter(@NonNull final List<T> list, @NonNull final BigDecimal price) {
        return list.stream()
                .filter(i -> i.getPrice().compareTo(price) > 0)
                .collect(Collectors.toList());
    }

    public Optional<Integer> sumVehicle(@NonNull final List<T> list) {
        return list.stream()
                .map(Vehicle::getCount)
                .reduce(Integer::sum);
    }

    public Map<String, VehicleType> sort(@NonNull final List<T> list) {
        return list.stream()
                .distinct()
                .sorted(Comparator.comparing(Vehicle::getModel))
                .collect(Collectors.toMap(Vehicle::getId, Vehicle::getType, (v1, v2) -> v1, LinkedHashMap::new));
    }

    public boolean checkDetails(@NonNull final List<T> list, @NonNull final String detail) {
        return list.stream()
                .map(Vehicle::getDetails)
                .flatMap(Collection::stream)
                .anyMatch(y -> y.equals(detail));
    }

    public DoubleSummaryStatistics statistics(@NonNull final List<T> list) {
        return list.stream()
                .map(Vehicle::getPrice)
                .mapToDouble(BigDecimal::doubleValue)
                .summaryStatistics();
    }

    public boolean checkPrice(@NonNull final List<T> list, @NonNull final Predicate<BigDecimal> criteria) {
        return list.stream()
                .map(Vehicle::getPrice)
                .anyMatch(criteria);
    }
}