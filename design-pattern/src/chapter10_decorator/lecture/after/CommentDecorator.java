package chapter10_decorator.lecture.after;

public class CommentDecorator implements CommentService {

    private CommentService commentService;

    public CommentDecorator(final CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public void addComment(final String comment) {
        commentService.addComment(comment);
    }
}
