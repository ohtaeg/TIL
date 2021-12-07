package chapter7_state.lecture.before;

import chapter7_state.lecture.before.OnlineCourse.State;

// 학생이 강의를 수강할 수 있다.
public class Client {

    public static void main(String[] args) {
        Student student = new Student("student");
        OnlineCourse onlineCourse = new OnlineCourse();

        Student ohtaeg = new Student("taegyeoung");
//        ohtaeg.addPrivateCourse(onlineCourse);
//
        onlineCourse.addStudent(student);
        onlineCourse.changeState(State.PRIVATE);
//
        onlineCourse.addStudent(ohtaeg);
        onlineCourse.addReview("hello", student);

        System.out.println(onlineCourse.getState());
        System.out.println(onlineCourse.getStudents());
        System.out.println(onlineCourse.getReviews());
    }

}
