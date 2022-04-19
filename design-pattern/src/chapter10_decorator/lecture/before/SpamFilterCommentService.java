package chapter10_decorator.lecture.before;

public class SpamFilterCommentService extends CommentService {

    @Override
    public void addComment(final String comment) {
        if (!isSpam(comment)) {
            super.addComment(comment);
        }
    }

    private boolean isSpam(final String comment) {
        return comment.contains("http");
    }
}
