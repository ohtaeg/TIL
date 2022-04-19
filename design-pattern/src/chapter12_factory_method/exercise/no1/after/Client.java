package chapter12_factory_method.exercise.no1.after;

public class Client {

    public static void main(String[] args) {
        MotorFactory motorFactory = AbstractMotorFactory.findByName("lg");
        Motor lgMotor = motorFactory.getMotor();
        ElevatorController controller = new ElevatorController(1, lgMotor);
        controller.gotoFloor(5);
        controller.gotoFloor(3);

        motorFactory = AbstractMotorFactory.findByName("hyundai");
        Motor hdMotor = motorFactory.getMotor();
        ElevatorController hdElevatorController = new ElevatorController(2, hdMotor);
        hdElevatorController.gotoFloor(4);
        hdElevatorController.gotoFloor(6);
    }
}
