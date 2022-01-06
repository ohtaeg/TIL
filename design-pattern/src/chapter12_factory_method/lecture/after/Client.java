package chapter12_factory_method.lecture.after;

public class Client {

    public static void main(String[] args) {
        Client client = new Client();

        Ship whiteShip = new WhiteShipFactory().order("whiteship", "asdf@naver.com");
        System.out.println(whiteShip);

        Ship blackShip = new BlackShipFactory().order("blackship", "asdf1@naver.com");
        System.out.println(blackShip);
    }

}
