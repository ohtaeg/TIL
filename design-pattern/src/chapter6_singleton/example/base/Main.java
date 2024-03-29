package chapter6_singleton.example.base;

public class Main {
    private static final int USER_NUM = 5;

    public static void main(String[] args) {
        User[] users = new User[USER_NUM];

        for (int i = 0; i < USER_NUM; i++) {
            users[i] = new User(i + 1 + "-user");
            users[i].print();
        }
    }
}
