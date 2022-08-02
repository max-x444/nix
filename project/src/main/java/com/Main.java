package com;

import com.model.Airplane;
import com.model.Auto;
import com.model.Manufacturer;
import com.model.Motorbike;
import com.model.Vehicle;
import com.model.VehicleType;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.utils.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();

    public static void main(String[] args) {
        Motorbike motorbike1 = MOTORBIKE_SERVICE.create("S 3500 RR", Manufacturer.BMW, BigDecimal.valueOf(6), 55.0, 1);
        Motorbike motorbike2 = MOTORBIKE_SERVICE.create("S 3400 RR", Manufacturer.BMW, BigDecimal.valueOf(3), 35.0, 3);
        Motorbike motorbike3 = MOTORBIKE_SERVICE.create("S 3400 RR", Manufacturer.BMW, BigDecimal.valueOf(700), 35.0, 3);
        Motorbike motorbike4 = MOTORBIKE_SERVICE.create("S 3400 RR", Manufacturer.BMW, BigDecimal.valueOf(1), 35.0, 3);
        Airplane airplane = AIRPLANE_SERVICE.create("Боинг-777", Manufacturer.BMW, BigDecimal.valueOf(11), 1000, 1);
        List<Auto> autoList = AUTO_SERVICE.create(1);

        motorbike2.setDetails(List.of("Engine", "Transmission"));
        motorbike3.setDetails(List.of("Transmission"));
        motorbike4.setDetails(List.of("Frames", "Engine"));
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(motorbike1);
        vehicleList.add(motorbike2);
        vehicleList.add(airplane);
        vehicleList.add(motorbike3);
        vehicleList.add(autoList.get(0));
        vehicleList.add(motorbike4);
        MOTORBIKE_SERVICE.print();
        AUTO_SERVICE.print();
        AIRPLANE_SERVICE.print();

        StreamUtil<Vehicle> streamUtil = new StreamUtil<>();
        BigDecimal price = BigDecimal.valueOf(100);
        LOGGER.info("Vehicles that cost more than {}", price);
        for (Vehicle vehicle : streamUtil.filter(vehicleList, price)) {
            LOGGER.info("Model: {}, Price: {}", vehicle.getModel(), vehicle.getPrice());
        }
        Optional<Integer> optional = streamUtil.sumVehicle(vehicleList);
        optional.ifPresent(x -> LOGGER.info("Sum vehicle: {}", x));
        LOGGER.info("Sorted vehicles");
        for (Map.Entry<String, VehicleType> entry : streamUtil.sort(vehicleList).entrySet()) {
            LOGGER.info("Key: {}, Value: {}", entry.getKey(), entry.getValue());
        }
        String detail = "Engine";
        LOGGER.info("The \"{}\" part is in one of the vehicles: {}", detail,
                streamUtil.checkDetails(vehicleList, detail));
        LOGGER.info("Statistics: {}", streamUtil.statistics(vehicleList));
        LOGGER.info("Checking for the existence of a vehicle price: {}",
                streamUtil.checkPrice(vehicleList, x -> x.compareTo(BigDecimal.ZERO) > 0));

        Map<String, Object> map = new HashMap<>();
        map.put("model", motorbike1.getModel());
        map.put("manufacturer", motorbike1.getManufacturer());
        map.put("price", motorbike1.getPrice());
        map.put("leanAngle", motorbike1.getLeanAngle());
        map.put("count", motorbike1.getCount());
        LOGGER.info("Creating motorbike with using a function: {}", MOTORBIKE_SERVICE.function.apply(map));
    }
}