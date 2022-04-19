package chapter8_command.exercise.no2;

public class Client {

    public static void main(String[] args) {
        Tv tv = new Tv();
        Command powerCommand = new PowerControlCommand(tv);
        Command muteCommand = new MuteCommand(tv);
        TwoButtonController controller = new TwoButtonController(powerCommand, muteCommand);

        // 2-5
        controller.setCommand(powerCommand, powerCommand);
        controller.button1Pressed();
        controller.button2Pressed();
        controller.button1Pressed();
        controller.button1Pressed();
        controller.button2Pressed();
        controller.button1Pressed();

        System.out.println();

        // 2-6
        controller.setCommand(muteCommand, powerCommand);
        controller.button1Pressed();
        controller.button2Pressed();
        controller.button1Pressed();
        controller.button1Pressed();
        controller.button2Pressed();
        controller.button1Pressed();
    }
}
