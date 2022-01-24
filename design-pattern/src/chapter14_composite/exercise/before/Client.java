package chapter14_composite.exercise.before;

public class Client {

    public static void main(String[] args) {
        Directory root = new Directory("Root");
        Directory sub = new Directory("sub");

        File file1 = new File("a", 1);
        File file2 = new File("b", 1);
        File file3 = new File("c", 1);

        root.addEntry(file1);
        root.addEntry(file2);
        root.addEntry(file3);

        root.addEntry(sub);

        File subFile = new File("aa", 2);
        sub.addEntry(subFile);

        root.print();
    }
}
