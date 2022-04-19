package chapter12_factory_method.exercise.no1.before;

public class LGMotor extends Motor {

    @Override
    protected void moveMotor(final Direction direction) {
        System.out.println("move LG Motor" + direction);
    }
}
