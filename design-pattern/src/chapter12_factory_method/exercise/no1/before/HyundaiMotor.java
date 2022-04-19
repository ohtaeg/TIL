package chapter12_factory_method.exercise.no1.before;

public class HyundaiMotor extends Motor{

    @Override
    protected void moveMotor(final Direction direction) {
        System.out.println("move Hyundai Motor " + direction);
    }
}
