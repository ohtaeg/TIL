package chapter9_observer.lecture.exercise.no2;


public class Client {

    public static void main(String[] args) {
        ElevatorController controller = new ElevatorController();
        Observer elevatorDisplay = new ElevatorDisplay();
        Observer voiceNotice = new VoiceNotice();
        Observer floorDisplay = new FloorDisplay();
        Observer controlRoomDisplay = new ControlRoomDisplay();

        controller.attach(elevatorDisplay);
        controller.attach(voiceNotice);
        controller.attach(floorDisplay);
        controller.attach(controlRoomDisplay);

        controller.gotoFloor(5);
        controller.gotoFloor(10);
    }

}
