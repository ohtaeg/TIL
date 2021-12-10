package chapter8_command.exercise.no3;

public class ElevatorController {

    private final int id;
    private int currentFloor;

    public ElevatorController(final int id) {
        this.id = id;
    }

    public void gotoFloor(final int floor) {
        currentFloor = floor;
        System.out.println("[" + id + "] - " + currentFloor);
    }

    public int diffFloor(final int destinationFloor) {
        return Math.abs(currentFloor - destinationFloor);
    }

    public boolean isEqualId(final int elevatorId) {
        return id == elevatorId;
    }

    public int getId() {
        return id;
    }
}
