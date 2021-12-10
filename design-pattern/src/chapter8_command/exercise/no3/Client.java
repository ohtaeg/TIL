package chapter8_command.exercise.no3;

import chapter8_command.exercise.no3.command.Command;
import chapter8_command.exercise.no3.command.DestinationSelectionCommand;
import chapter8_command.exercise.no3.command.ElevatorRequestCommand;
import chapter8_command.exercise.no3.direction.Direction;

public class Client {

    public static void main(String[] args) {
        ElevatorController controller1 = new ElevatorController(1);
        ElevatorController controller2 = new ElevatorController(2);

        ElevatorManager manager = new ElevatorManager(2);
        manager.addController(controller1);
        manager.addController(controller2);

        // 1층 버튼, 1번 엘베
        Command destinationCommand1st = new DestinationSelectionCommand(1, controller1);
        ElevatorButton elevatorButton= new ElevatorButton(destinationCommand1st);
        elevatorButton.press();


        // 2층에서 아래로, 어떤 컨트롤러가 와야할까? 가장 가까운 컨트롤러
        Command destinationCommand2nd = new DestinationSelectionCommand(2, controller2);
        elevatorButton= new ElevatorButton(destinationCommand2nd);
        elevatorButton.press();

        Command requestDownCommand = new ElevatorRequestCommand(2, Direction.DOWN, manager);
        elevatorButton= new ElevatorButton(requestDownCommand);
        elevatorButton.press();
    }

}
