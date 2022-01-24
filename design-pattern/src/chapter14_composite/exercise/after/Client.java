package chapter14_composite.exercise.after;

public class Client {

    public static void main(String[] args) {
        ProgramFile rootDirectory = new Directory("Root");
        ProgramFile sub = new Directory("sub");

        ProgramFile file1 = new File("a", 1);
        ProgramFile file2 = new File("b", 1);
        ProgramFile file3 = new File("c", 1);

        rootDirectory.addEntry(file1);
        rootDirectory.addEntry(file2);
        rootDirectory.addEntry(file3);

        rootDirectory.addEntry(sub);

        ProgramFile subFile = new File("aa", 2);
        sub.addEntry(subFile);

        rootDirectory.print();
    }
}
