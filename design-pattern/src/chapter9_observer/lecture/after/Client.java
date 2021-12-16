package chapter9_observer.lecture.after;

public class Client {

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        User user1 = new User("태경");
        User user2 = new User("ohtaeg");

        chatServer.register("오징어게임", user1);
        chatServer.register("오징어게임", user2);
        chatServer.register("디자인패턴", user1);

        chatServer.sendMessage(user1, "오징어게임", "우린 깐부자너");
        chatServer.sendMessage(user2, "디자인패턴", "옵저버 패턴으로 만든 채팅");

    }

}
