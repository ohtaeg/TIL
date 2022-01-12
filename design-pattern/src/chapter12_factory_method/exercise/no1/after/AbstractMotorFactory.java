package chapter12_factory_method.exercise.no1.after;

import java.util.Arrays;

public enum AbstractMotorFactory implements MotorFactory {
    LG("LG") {
        @Override
        public Motor getMotor() {
            return new LGMotor();
        }
    }, HYUNDAI("HYUNDAI") {
        @Override
        public Motor getMotor() {
            return new HyundaiMotor();
        }
    };

    private String vendorName;

    AbstractMotorFactory(final String vendorName) {
        this.vendorName = vendorName;
    }

    public static AbstractMotorFactory findByName(final String name) {
        return Arrays.stream(AbstractMotorFactory.values())
                     .filter(motorFactory -> motorFactory.vendorName.equalsIgnoreCase(name))
                     .findFirst()
                     .orElseThrow();
    }

}
