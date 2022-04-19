package chapter10_decorator.lecture.after;

public class DefaultCommentService implements CommentService {

    @Override
    public void addComment(final String comment) {
        System.out.println(comment);
    }
}
