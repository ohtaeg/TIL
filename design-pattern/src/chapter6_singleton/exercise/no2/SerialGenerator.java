package chapter6_singleton.exercise.no2;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SerialGenerator {
    private static final Set<Integer> serials = new HashSet<>();

    private SerialGenerator() {}

    public static int create() {
        int serial = UUID.randomUUID().variant();
        while (serials.contains(serial)) {
            serial = UUID.randomUUID().variant();
        }
        serials.add(serial);
        return serial;
    }
}
