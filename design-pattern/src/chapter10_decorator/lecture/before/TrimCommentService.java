package chapter10_decorator.lecture.before;

public class TrimCommentService extends CommentService {

    @Override
    public void addComment(final String comment) {
        super.addComment(trim(comment));
    }

    private String trim(final String comment) {
        return comment.replace(" ", "");
    }
}
