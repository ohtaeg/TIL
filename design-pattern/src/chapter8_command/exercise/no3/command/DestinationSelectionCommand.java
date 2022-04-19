package chapter8_command.exercise.no3.command;

import chapter8_command.exercise.no3.ElevatorController;

public class DestinationSelectionCommand implements Command {

    private ElevatorController controller;
    private int floor;

    public DestinationSelectionCommand(final int floor, final ElevatorController controller) {
        this.floor = floor;
        this.controller = controller;
    }

    @Override
    public void execute() {
        controller.gotoFloor(floor);
    }
}
