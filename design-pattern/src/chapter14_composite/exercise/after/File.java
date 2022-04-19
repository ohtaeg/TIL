package chapter14_composite.exercise.after;

import java.util.stream.IntStream;

public class File extends ProgramFile {

    private int size;

    public File(final String name) {
        super(name);
        this.size = 0;
    }

    public File(final String name, final int size) {
        super(name);
        this.size = size;
    }

    public String getName() {
        return super.name;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void print() {
        IntStream.range(0, depth).forEach(index -> System.out.print("\t"));
        System.out.println("[File] " + name + ", Size : " + size);
    }
}
