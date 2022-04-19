package chapter7_state.lecture.after;

public interface State {
    void addReview(String review, Student student);

    void addStudent(Student student);
}
