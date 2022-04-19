package chapter7_state.lecture.after.state;

import chapter7_state.lecture.after.OnlineCourse;
import chapter7_state.lecture.after.State;
import chapter7_state.lecture.after.Student;

public class Private implements State {

    private OnlineCourse onlineCourse;

    public Private(final OnlineCourse onlineCourse) {
        this.onlineCourse = onlineCourse;
    }

    @Override
    public void addReview(final String review, final Student student) {
        if (this.onlineCourse.getStudents().contains(student)) {
            this.onlineCourse.getReviews().add(review);
        } else {
            throw new UnsupportedOperationException("private 강좌는 수강하는 사람만 리뷰를 남길 수 있습니다.");
        }
    }

    @Override
    public void addStudent(final Student student) {
        if (student.isAvailable(this.onlineCourse)) {
            this.onlineCourse.getStudents().add(student);
        } else {
            throw new UnsupportedOperationException("private 강좌를 수강할 수 없습니다.");
        }
    }
}
