package chapter7_state.lecture.after;

import java.util.HashSet;
import java.util.Set;

public class Student {

    private Set<OnlineCourse> onlineCourses = new HashSet<>();
    private String name;

    public Student(final String name) {
        this.name = name;
    }

    public boolean isAvailable(OnlineCourse onlineCourse) {
        return onlineCourses.contains(onlineCourse);
    }

    public void addPrivate(OnlineCourse onlineCourse) {
        this.onlineCourses.add(onlineCourse);
    }

    @Override
    public String toString() {
        return "Student{" +
            "onlineCourses=" + onlineCourses +
            ", name='" + name + '\'' +
            '}';
    }
}
