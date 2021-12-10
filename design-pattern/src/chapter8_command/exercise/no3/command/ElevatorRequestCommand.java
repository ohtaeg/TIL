package chapter8_command.exercise.no3.command;

import chapter8_command.exercise.no3.ElevatorManager;
import chapter8_command.exercise.no3.direction.Direction;

public class ElevatorRequestCommand implements Command {

    private int floor;
    private Direction direction;
    private ElevatorManager manager;

    public ElevatorRequestCommand(final int destination, final Direction direction, final ElevatorManager manager) {
        this.floor = destination;
        this.direction = direction;
        this.manager = manager;
    }

    @Override
    public void execute() {
        manager.requestElevator(floor, direction);
    }
}
