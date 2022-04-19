package chapter10_decorator.lecture.after;

public class TrimmingDecorator extends CommentDecorator {

    public TrimmingDecorator(final CommentService commentService) {
        super(commentService);
    }

    @Override
    public void addComment(final String comment) {
        super.addComment(trim(comment));
    }

    private String trim(final String comment) {
        return comment.replace(" ", "");
    }
}
