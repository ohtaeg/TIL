package chapter12_factory_method.exercise.no1.before;

public abstract class Motor {
    private MotorStatus motorStatus;

    public Motor() {
        this.motorStatus = MotorStatus.STOPPED;
    }

    public Motor(final MotorStatus motorStatus) {
        this.motorStatus = motorStatus;
    }

    public MotorStatus getMotorStatus() {
        return motorStatus;
    }

    public void setMotorStatus(final MotorStatus motorStatus) {
        this.motorStatus = motorStatus;
    }

    public void move(Direction direction) {
        MotorStatus motorStatus = getMotorStatus();
        if (motorStatus == MotorStatus.MOVING) {
            return;
        }

        moveMotor(direction);
        setMotorStatus(MotorStatus.MOVING);
    }

    public void stop() {
        this.motorStatus = MotorStatus.STOPPED;
    }

    protected abstract void moveMotor(Direction direction);
}
