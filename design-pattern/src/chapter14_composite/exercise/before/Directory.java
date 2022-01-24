package chapter14_composite.exercise.before;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Directory {
    private String name;
    private int depth;
    private List<Object> entries = new ArrayList<>();

    public Directory(final String name) {
        this.name = name;
    }

    public void addEntry(Object object) {
        entries.add(object);

        if (object instanceof File) {
            ((File) object).setDepth(depth + 1);
        }

        if (object instanceof Directory) {
            ((Directory) object).setDepth(depth + 1);
        }
    }

    public void removeEntry(Object entry) {
        this.entries.remove(entry);
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        int sum = 0;
        sum += fileStream().mapToInt(File::getSize).sum();
        sum += directoryStream().mapToInt(Directory::getSize).sum();
        return sum;
    }

    public void print() {
        IntStream.range(0, depth).forEach(index -> System.out.print("\t"));
        System.out.println("[Directory] " + name + ", Size : " + getSize());

        fileStream().forEach(File::print);
        directoryStream().forEach(Directory::print);
    }

    private void setDepth(final int depth) {
        this.depth = depth;
    }

    private Stream<File> fileStream() {
        return entries.stream()
                      .filter(obj -> obj instanceof File)
                      .map(obj -> (File) obj);
    }

    private Stream<Directory> directoryStream() {
        return entries.stream()
                      .filter(obj -> obj instanceof Directory)
                      .map(obj -> (Directory) obj);
    }
}