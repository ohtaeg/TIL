package chapter6_singleton.example.reflect;

import java.lang.reflect.Constructor;

public class SingletonReflectingAttack {
    public static void main (String[] args) {

        Printer instance = Printer.getPrinter();
        Printer otherInstance = null;

        try {
            Constructor[] constructors = Printer.class.getDeclaredConstructors();
            for (Constructor constructor : constructors) {
                constructor.setAccessible(true);
                otherInstance = (Printer) constructor.newInstance();
            }
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

        System.out.println("instance : " + instance.hashCode());
        if (otherInstance != null) {
            System.out.println("other : " + otherInstance.hashCode());
        }
    }
}