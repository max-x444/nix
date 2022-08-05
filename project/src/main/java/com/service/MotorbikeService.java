package com.service;

import com.model.Engine;
import com.model.Manufacturer;
import com.model.Motorbike;
import com.model.Vehicle;
import com.repository.CrudRepository;
import com.repository.MotorbikeRepository;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MotorbikeService extends VehicleService<Motorbike> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MotorbikeService.class);
    private String head;
    private String currency;
    private String tail;
    private static MotorbikeService instance;

    public MotorbikeService(CrudRepository<Motorbike> crudRepository) {
        super(crudRepository);
    }

    public static MotorbikeService getInstance() {
        if (instance == null) {
            instance = new MotorbikeService(new MotorbikeRepository());
        }
        return instance;
    }

    public Motorbike create(String model, Manufacturer manufacturer, BigDecimal price, Double leanAngle, int count,
                            LocalDateTime created, String currency, Engine engine) {
        Motorbike motorbike = new Motorbike(model, manufacturer, price, leanAngle, count, created, currency, engine);
        crudRepository.create(motorbike);
        return motorbike;
    }

    public Motorbike findOrCreateDefault(String id) {
        Optional<Motorbike> optionalMotorbike = crudRepository.findById(id).or(() -> Optional.of(createDefault()));
        optionalMotorbike.ifPresent(motorbike -> crudRepository.delete(motorbike));
        return optionalMotorbike.orElseGet(this::createDefault);
    }

    public boolean findOrException(String id) {
        crudRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find motorbike with id " + id));
        return true;
    }

    public boolean checkManufacturerById(String id, Manufacturer searchManufacturer) {
        AtomicBoolean temp = new AtomicBoolean(false);
        crudRepository.findById(id)
                .map(Vehicle::getManufacturer)
                .filter(m -> m.equals(searchManufacturer))
                .ifPresentOrElse(
                        manufacturer -> temp.set(true),
                        () -> temp.set(false)
                );
        return temp.get();
    }

    public List<String> readFile(@NonNull final File file) {
        List<String> list = new ArrayList<>();
        if (file.exists()) {
            try (FileReader fileReader = new FileReader(file);
                 BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    list.add(line);
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return list;
    }

    private String parseData(@NonNull final List<String> list, @NonNull final String regex) {
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        for (String s : list) {
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return "";
    }

    public Map<String, Object> createMap(@NonNull final List<String> list, @NonNull final String fileName) {
        final Map<String, Object> map = new HashMap<>();
        getRegex(fileName);
        try {
            return mapFilling(list, map);
        } catch (RuntimeException exception) {
            LOGGER.error("RuntimeException: " + exception.getMessage());
        }
        return map;
    }

    private Map<String, Object> mapFilling(@NonNull final List<String> list, @NonNull final Map<String, Object> map) {
        map.put("model", parseData(list, head + "[Mm][Oo][Dd][Ee][Ll]" + tail));
        map.put("manufacturer", Manufacturer.valueOf(parseData(list, head + "[Mm][Aa][Nn][Uu][Ff][Aa][Cc][Tt][Uu][Rr][Ee][Rr]" + tail)));
        map.put("price", BigDecimal.valueOf(Double.parseDouble(parseData(list, head + "[Pp][Rr][Ii][Cc][Ee].*" + tail))));
        map.put("leanAngle", Double.parseDouble(parseData(list, head + "[Ll][Ee][Aa][Nn][Aa][Nn][Gg][Ll][Ee]" + tail)));
        map.put("count", Integer.valueOf(parseData(list, head + "[Cc][Oo][Uu][Nn][Tt]" + tail)));
        map.put("created", LocalDateTime.parse(parseData(list, head + "[Cc][Rr][Ee][Aa][Tt][Ee][Dd]" + tail),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")));
        map.put("currency", parseData(list, currency));
        map.put("engine", new Engine(
                Integer.parseInt(parseData(list, head + "[Vv][Oo][Ll][Uu][Mm][Ee]" + tail)),
                parseData(list, head + "[Bb][Rr][Aa][Nn][Dd]" + tail)));
        return map;
    }

    private void getRegex(@NonNull final String fileName) {
        switch (fileName) {
            case "vehicle.xml" -> {
                head = "[<]";
                tail = "[>](.*)[<][/]";
                currency = "[Cc][Uu][Rr][Rr][Ee][Nn][Cc][Yy][=][\\\"](.*)[\\\"][>]";
            }
            case "vehicle.json" -> {
                head = "[\\\"]";
                tail = "[\\\"][:][ ][\\\"](.*)[\\\"]";
                currency = head + "[Cc][Uu][Rr][Rr][Ee][Nn][Cc][Yy]" + tail;
            }
            default -> {
                head = "";
                tail = "";
                currency = "";
            }
        }
    }

    public Function<Map<String, Object>, Motorbike> function = map -> new Motorbike(
            (String) map.getOrDefault("model", "Model"),
            (Manufacturer) map.getOrDefault("manufacturer", Manufacturer.BMW),
            (BigDecimal) map.getOrDefault("price", BigDecimal.ZERO),
            (Double) map.getOrDefault("leanAngle", 0.0),
            (int) map.getOrDefault("count", 0),
            (LocalDateTime) map.getOrDefault("created", LocalDateTime.now()),
            (String) map.getOrDefault("currency", "$"),
            (Engine) map.getOrDefault("engine", new Engine(0, "Brand"))
    );

    private Motorbike createDefault() {
        return new Motorbike(
                "Default model",
                Manufacturer.MERCEDES,
                BigDecimal.ZERO,
                0.0,
                0,
                LocalDateTime.now(),
                "$",
                new Engine(0, "Brand"));
    }
}