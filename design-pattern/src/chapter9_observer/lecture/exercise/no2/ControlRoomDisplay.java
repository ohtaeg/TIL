package chapter9_observer.lecture.exercise.no2;

public class ControlRoomDisplay implements Observer {

    @Override
    public void update(final ElevatorController elevatorController) {
        System.out.println(this.getClass().getSimpleName() + ", current : " + elevatorController.getCurrent());
    }
}
