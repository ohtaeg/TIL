package chapter12_factory_method.exercise.no1.before;

public class Client {

    public static void main(String[] args) {
        Motor lgMotor = new LGMotor();
        ElevatorController controller = new ElevatorController(1, lgMotor);
        controller.gotoFloor(5);
        controller.gotoFloor(3);

        Motor hdMotor = new HyundaiMotor();
        ElevatorController hdElevatorController = new ElevatorController(2, hdMotor);
        hdElevatorController.gotoFloor(4);
        hdElevatorController.gotoFloor(6);
    }
}
