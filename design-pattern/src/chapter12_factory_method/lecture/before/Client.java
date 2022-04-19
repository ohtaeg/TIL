package chapter12_factory_method.lecture.before;

public class Client {

    public static void main(String[] args) {
        Client client = new Client();

        Ship whiteShip = ShipFactories.order("whiteship", "asdf@naver.com");
        System.out.println(whiteShip);

        Ship blackShip = ShipFactories.order("blackship", "asdf1@naver.com");
        System.out.println(blackShip);
    }

}
