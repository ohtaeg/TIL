package chapter10_decorator.lecture.before;

public class Client {

    private CommentService commentService;

    public Client(final CommentService commentService) {
        this.commentService = commentService;
    }

    private void writeComment(String comment) {
        commentService.addComment(comment);
    }

    public static void main(String[] args) {
        Client client = new Client(new CommentService());
        // Client client = new Client(new SpamFilterCommentService()); // 이렇게 상속 구조로 필요한 기능을 갈아 끼우려고 하면 확장해나가기 쉽지 않다.
        client.writeComment("오징어 게임");
        client.writeComment(" 자네는 휴먼인가? ");
        client.writeComment("http://ohtaeg.com");
    }
}
