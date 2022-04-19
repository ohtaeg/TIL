package chapter9_observer.lecture.after;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatServer {
    private Map<String, List<Subscriber>> subscribers = new HashMap<>();

    public void register(String keyword, Subscriber subscriber) {
        if (subscribers.containsKey(keyword)) {
            subscribers.get(keyword).add(subscriber);
            return;
        }

        List<Subscriber> list = new ArrayList<>();
        list.add(subscriber);
        this.subscribers.put(keyword, list);
    }

    public void unregister(String keyword, Subscriber subscriber) {
        if (subscribers.containsKey(keyword)) {
            subscribers.get(keyword).remove(subscriber);
        }
    }

    public void sendMessage(User user, String keyword, String message) {
        if (subscribers.containsKey(keyword)) {
            String userMessage = user.getName() + ": " + message;
            this.subscribers.get(keyword).forEach(s -> s.handleMessage(userMessage));
        }
    }
}
