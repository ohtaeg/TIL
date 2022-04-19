package chapter14_composite.exercise.before;

import java.util.stream.IntStream;

public class File {
    private String name;
    private int size;
    private int depth = 0;

    public File(final String name, final int size) {
        this.name = name;
        this.size = size;
    }

    public void setDepth(final int depth) {
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void print() {
        IntStream.range(0, depth).forEach(index -> System.out.print("\t"));
        System.out.println("[File] " + name + ", Size : " + size);
    }
}
