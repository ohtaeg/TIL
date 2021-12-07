package chapter7_state.lecture.after.state;

import chapter7_state.lecture.after.OnlineCourse;
import chapter7_state.lecture.after.State;
import chapter7_state.lecture.after.Student;

public class Published implements State {

    private OnlineCourse onlineCourse;

    public Published(final OnlineCourse onlineCourse) {
        this.onlineCourse = onlineCourse;
    }

    @Override
    public void addReview(final String review, final Student student) {
        this.onlineCourse.getReviews().add(review);
    }

    @Override
    public void addStudent(final Student student) {
        this.onlineCourse.getStudents().add(student);
    }
}
