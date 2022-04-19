package chapter11_template.lecture.after;

public class Client {

    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor("number.txt");
        int result = fileProcessor.process((sum, line) -> sum += Integer.parseInt(line));
        System.out.println(result);
    }

}
