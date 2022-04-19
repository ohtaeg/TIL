package chapter14_composite.example.before;

public class Client {

    public static void main(String[] args) {
        Body body = new Body(100, 70);
        Keyboard keyboard = new Keyboard(5, 2);
        Monitor monitor = new Monitor(20, 30);
        Speaker speaker = new Speaker(4, 2);

        Computer computer = new Computer();
        computer.setBody(body);
        computer.setKeyboard(keyboard);
        computer.setMonitor(monitor);

        int price = computer.getPrice();
        int power = computer.getPower();

        System.out.println(price + "만 원");
        System.out.println(power + "W");
    }

}
