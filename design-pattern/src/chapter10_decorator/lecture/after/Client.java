package chapter10_decorator.lecture.after;

public class Client {
    private CommentService component;

    public Client(final CommentService component) {
        this.component = component;
    }

    private void writeComment(String comment) {
        component.addComment(comment);
    }

    public static void main(String[] args) {
        CommentService defaultCommentService = new DefaultCommentService();

        Client client = new Client(defaultCommentService);
        client.writeComment("오징어 게임");

        Client client2 = new Client(new TrimmingDecorator(new DefaultCommentService()));
        client2.writeComment(" 자네는 휴먼인가? ");

        Client client3 = new Client(new SpamFilteringCommentDecorator(new DefaultCommentService()));
        client3.writeComment("http://ohtaeg.com");
    }
}
