package com;

import com.command.Action;
import com.command.Command;
import com.model.Airplane;
import com.model.Auto;
import com.model.Manufacturer;
import com.model.Motorbike;
import com.model.Vehicle;
import com.service.AirplaneService;
import com.service.AutoService;
import com.service.MotorbikeService;
import com.utils.BinaryTree;
import com.utils.UserInputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final AutoService AUTO_SERVICE = AutoService.getInstance();
    private static final AirplaneService AIRPLANE_SERVICE = AirplaneService.getInstance();
    private static final MotorbikeService MOTORBIKE_SERVICE = MotorbikeService.getInstance();

    public static void main(String[] args) {
        Motorbike motorbike1 = MOTORBIKE_SERVICE.create("S 3500 RR", Manufacturer.BMW, BigDecimal.valueOf(6), 55.0, 1);
        Motorbike motorbike2 = MOTORBIKE_SERVICE.create("S 3400 RR", Manufacturer.BMW, BigDecimal.valueOf(3), 35.0, 3);
        Motorbike motorbike3 = MOTORBIKE_SERVICE.create("S 3400 RR", Manufacturer.BMW, BigDecimal.valueOf(7), 35.0, 3);
        Motorbike motorbike4 = MOTORBIKE_SERVICE.create("S 3400 RR", Manufacturer.BMW, BigDecimal.valueOf(1), 35.0, 3);
        Airplane airplane = AIRPLANE_SERVICE.create("Боинг-777", Manufacturer.BMW, BigDecimal.valueOf(11), 1000, 1);
        List<Auto> autoList = AUTO_SERVICE.create(1);


        Comparator<Vehicle> comparator = Comparator.comparing(Vehicle::getPrice)
                .reversed()
                .thenComparing(vehicle -> vehicle.getClass().getSimpleName())
                .thenComparing(Vehicle::getCount);

        BinaryTree<Vehicle> binaryTree = new BinaryTree<>(comparator);
        binaryTree.add(motorbike1);
        binaryTree.add(motorbike2);
        binaryTree.add(airplane);
        binaryTree.add(motorbike3);
        binaryTree.add(autoList.get(0));
        binaryTree.add(motorbike4);
        binaryTree.print();

        LOGGER.info("The cost of the left branch of the binary tree: {}", binaryTree.getValueLeftBranch());
        LOGGER.info("The cost of the right branch of the binary tree: {}", binaryTree.getValueRightBranch());

        final Action[] actions = Action.values();
        Command command;
        do {
            command = actions[UserInputUtil.getNames("What you want:", actions)].execute();
        } while (command != null);
    }
}