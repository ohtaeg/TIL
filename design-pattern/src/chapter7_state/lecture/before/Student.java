package chapter7_state.lecture.before;

public class Student {
    private final String name;

    public Student(final String name) {
        this.name = name;
    }

    public void addPrivateCourse(final OnlineCourse onlineCourse) {
    }

    @Override
    public String toString() {
        return "Student{" +
            "name='" + name + '\'' +
            '}';
    }
}
