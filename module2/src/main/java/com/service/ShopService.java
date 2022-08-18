package com.service;

import com.exception.InvalidStringException;
import com.model.Electronics;
import com.model.Invoice;
import com.model.Telephone;
import com.model.Television;
import com.model.constants.Country;
import com.model.constants.Manufacture;
import com.model.constants.ScreenType;
import com.model.constants.Type;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public class ShopService<T extends Electronics> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopService.class);
    private static final Random RANDOM = new Random();
    private final List<Invoice<T>> invoiceList = new ArrayList<>();
    private final List<T> electronicsList = new ArrayList<>();
    private final Predicate<BigDecimal> criteria;
    private final int maxListSize;
    private final int minListSize;
    private int maxCountElectronics;

    public ShopService(Predicate<BigDecimal> criteria, int maxListSize, int minListSize) {
        this.criteria = criteria;
        this.maxListSize = maxListSize;
        this.minListSize = minListSize;
        this.maxCountElectronics = RANDOM.nextInt(maxListSize) + minListSize;
    }

    public List<Invoice<T>> readFile(@NonNull final File file) {
        if (file.exists()) {
            try (final FileReader fileReader = new FileReader(file);
                 final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                final Map<String, String> map = new LinkedHashMap<>();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (addElectronicToList(map, line)) {
                        checkSizeList();
                    }
                }
            } catch (IOException exception) {
                LOGGER.error(exception.getMessage());
            } finally {
                if (!electronicsList.isEmpty()) {
                    invoiceList.add(createInvoice());
                }
            }
        }
        return invoiceList;
    }

    private void checkSizeList() {
        if (maxCountElectronics == electronicsList.size()) {
            invoiceList.add(createInvoice());
            maxCountElectronics = RANDOM.nextInt(maxListSize) + minListSize;
            electronicsList.clear();
        }
    }

    private boolean addElectronicToList(@NonNull final Map<String, String> map, @NonNull final String line) {
        final String[] strings = line.split(",");
        if (map.isEmpty()) {
            for (String string : strings) {
                map.put(string, "");
            }
        } else {
            try {
                if (checkValidString(map, strings)) {
                    return electronicsList.add(createElectronic(map));
                }
            } catch (InvalidStringException exception) {
                LOGGER.error(exception.getMessage());
            }
        }
        return false;
    }

    private boolean checkValidString(@NonNull final Map<String, String> map, @NonNull final String[] strings)
            throws InvalidStringException {
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (strings[index].isEmpty()) {
                throw new InvalidStringException("The string is empty");
            }
            map.put(entry.getKey(), strings[index++]);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private T createElectronic(@NonNull final Map<String, String> map) throws InvalidStringException {
        try {
            switch (map.get("type")) {
                case "Telephone" -> {
                    return (T) new Telephone(
                            map.get("series"),
                            ScreenType.valueOf(map.get("screen type").toUpperCase()),
                            BigDecimal.valueOf(Double.parseDouble(String.valueOf(map.get("price")))),
                            Manufacture.valueOf(map.get("model").toUpperCase()));
                }
                case "Television" -> {
                    return (T) new Television(
                            map.get("series"),
                            ScreenType.valueOf(map.get("screen type").toUpperCase()),
                            BigDecimal.valueOf(Double.parseDouble(String.valueOf(map.get("price")))),
                            Integer.parseInt(map.get("diagonal").toUpperCase()),
                            Country.valueOf(map.get("country").toUpperCase()));
                }
                default -> throw new InvalidStringException("Invalid string: " + map.get("type"));
            }
        } catch (RuntimeException exception) {
            throw new InvalidStringException(exception.getMessage());
        }
    }

    private Invoice<T> createInvoice() {
        return new Invoice<>(electronicsList, PersonService.create(), getType());
    }

    private Type getType() {
        if (criteria.test(getTotalSum())) {
            return Type.WHOLESALE;
        } else {
            return Type.RETAIL;
        }
    }

    private BigDecimal getTotalSum() {
        return electronicsList.stream()
                .map(Electronics::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}