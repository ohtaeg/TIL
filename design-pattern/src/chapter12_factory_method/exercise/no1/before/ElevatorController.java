package chapter12_factory_method.exercise.no1.before;

public class ElevatorController {
    private int id;
    private int curFloor;
    private Motor motor;

    public ElevatorController(final int id, final Motor motor) {
        this.id = id;
        this.motor = motor;
    }

    public void gotoFloor(int destination) {
        if (destination == curFloor) {
            return;
        }

        Direction direction;

        if (destination < curFloor) {
            direction = Direction.UP;
        } else {
            direction = Direction.DOWN;
        }

        motor.move(direction);

        System.out.println("Elevator [" + id + "] Floor : " + curFloor);
        curFloor = destination;
        System.out.println("==> [" + curFloor + "] with " + motor.getClass().getName());
        motor.stop();
    }
}
