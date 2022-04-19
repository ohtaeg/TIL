package chapter11_template.lecture.after;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileProcessor {

    private String path;

    public FileProcessor(final String path) {
        this.path = path;
    }

    public int process(Callback callback) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            int result = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result = callback.getResult(result, line);
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(path + "에 해당하는 파일이 없습니다.", e);
        }
    }
}
