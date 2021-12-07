package chapter7_state.lecture.after;

import chapter7_state.lecture.after.state.Draft;
import java.util.ArrayList;
import java.util.List;

public class OnlineCourse {
    private State state;
    private List<Student> students = new ArrayList<>();
    private List<String> reviews = new ArrayList<>();

    public OnlineCourse() {
        this.state = new Draft(this);
    }

    public void addStudent(Student student) {
        this.state.addStudent(student);
    }

    public void addReview(String review, Student student) {
        this.state.addReview(review, student);
    }

    public State getState() {
        return state;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void changeState(final State newState) {
        this.state = newState;
    }
}
