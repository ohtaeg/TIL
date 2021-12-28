package chapter10_decorator.lecture.after;

public class SpamFilteringCommentDecorator extends CommentDecorator{

    public SpamFilteringCommentDecorator(final CommentService commentService) {
        super(commentService);
    }

    @Override
    public void addComment(final String comment) {
        if (isNotSpam(comment)) {
            super.addComment(comment);
        }
    }

    private boolean isNotSpam(final String comment) {
        return !comment.contains("http");
    }
}
