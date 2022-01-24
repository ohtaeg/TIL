package chapter14_composite.exercise.after;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Directory extends ProgramFile {

    private List<ProgramFile> programFiles = new ArrayList<>();

    public Directory(final String name) {
        super(name);
    }

    public void addEntry(ProgramFile programFile) {
        programFile.setDepth(depth + 1);
        programFiles.add(programFile);
    }

    @Override
    public int getSize() {
        int sum = 0;
        sum += directoryStream().mapToInt(Directory::getSize).sum();
        return sum + fileStream().mapToInt(File::getSize).sum();
    }

    @Override
    public void print() {
        IntStream.range(0, depth).forEach(index -> System.out.print("\t"));
        System.out.println("[Directory] " + name + ", Size : " + getSize());

        fileStream().forEach(File::print);
        directoryStream().forEach(Directory::print);
    }

    private Stream<File> fileStream() {
        return programFiles.stream()
                           .filter(obj -> obj instanceof File)
                           .map(obj -> (File) obj);
    }

    private Stream<Directory> directoryStream() {
        return programFiles.stream()
                           .filter(obj -> obj instanceof Directory)
                           .map(obj -> (Directory) obj);
    }
}