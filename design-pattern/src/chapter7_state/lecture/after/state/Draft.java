package chapter7_state.lecture.after.state;

import chapter7_state.lecture.after.OnlineCourse;
import chapter7_state.lecture.after.State;
import chapter7_state.lecture.after.Student;

public class Draft implements State {

    private OnlineCourse onlineCourse;

    public Draft(final OnlineCourse onlineCourse) {
        this.onlineCourse = onlineCourse;
    }

    @Override
    public void addReview(final String review, final Student student) {
        throw new UnsupportedOperationException("드래프트 상태에서는 리뷰를 남길 수 없습니다.");
    }

    @Override
    public void addStudent(final Student student) {
        this.onlineCourse.getStudents().add(student);
        if (this.onlineCourse.getStudents().size() > 1) {
            this.onlineCourse.changeState(new Private(this.onlineCourse));
        }
    }
}
