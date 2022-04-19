package chapter8_command.exercise.no3;

import chapter8_command.exercise.no3.direction.Direction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ElevatorManager {

    private final List<ElevatorController> controllers;
    private final int count;

    public ElevatorManager(final int count) {
        this.count = count;
        this.controllers = new ArrayList<>(count);
    }

    public void addController(final ElevatorController controller) {
        controllers.add(controller);
    }

    public void requestElevator(final int destinationFloor, final Direction direction) {
        final int elevatorId = selectElevator(destinationFloor, direction);
        ElevatorController target = null;
        for (ElevatorController controller : controllers) {
            if (controller.isEqualId(elevatorId)) {
                target = controller;
                break;
            }
        }

        target.gotoFloor(destinationFloor);
    }

    private int selectElevator(final int destinationFloor, final Direction direction) {
        Map<ElevatorController, Integer> closets = new HashMap<>();
        int min = Integer.MAX_VALUE;
        for (ElevatorController controller : controllers) {
            int distance = controller.diffFloor(destinationFloor);
            min = Math.min(distance, min);
            closets.put(controller, min);
        }

        return Collections.min(closets.entrySet(), Entry.comparingByValue()).getKey().getId();
    }
}
