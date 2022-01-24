package chapter14_composite.exercise.after;

public abstract class ProgramFile {
    protected String name;
    protected int depth;

    public ProgramFile(final String name) {
        this.name = name;
    }

    public void setDepth(final int depth) {
        this.depth = depth;
    }

    public void addEntry(ProgramFile programFile) {
        this.addEntry(programFile);
    }

    public abstract int getSize();

    public abstract void print();
}
