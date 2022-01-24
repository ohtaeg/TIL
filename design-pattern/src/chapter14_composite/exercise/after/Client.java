package chapter14_composite.exercise.after;

public class Client {

    public static void main(String[] args) {
        Directory rootDirectory = new Directory("Root");
        Directory sub = new Directory("sub");

        File file1 = new File("a", 1);
        File file2 = new File("b", 1);
        File file3 = new File("c", 1);

        rootDirectory.addEntry(file1);
        rootDirectory.addEntry(file2);
        rootDirectory.addEntry(file3);

        rootDirectory.addEntry(sub);

        File subFile = new File("aa", 2);
        sub.addEntry(subFile);

        rootDirectory.print();
    }
}
