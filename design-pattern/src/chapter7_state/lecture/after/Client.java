package chapter7_state.lecture.after;

import chapter7_state.lecture.after.state.Private;

public class Client {

    public static void main(String[] args) {
        Student student = new Student("student");
        OnlineCourse onlineCourse = new OnlineCourse();
        Student ohtaeg = new Student("taegyeoung");

        onlineCourse.addStudent(student);
        onlineCourse.changeState(new Private(onlineCourse));
        onlineCourse.addReview("hello", student);

        ohtaeg.addPrivate(onlineCourse);
        onlineCourse.addStudent(ohtaeg);

        System.out.println(onlineCourse.getState());
        System.out.println(onlineCourse.getStudents());
        System.out.println(onlineCourse.getReviews());
    }
}
