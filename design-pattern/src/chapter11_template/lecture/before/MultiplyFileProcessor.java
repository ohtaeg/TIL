package chapter11_template.lecture.before;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MultiplyFileProcessor {

    private String path;

    public MultiplyFileProcessor(final String path) {
        this.path = path;
    }

    public int process() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
            int result = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result *= Integer.parseInt(line);
            }
            return result;
        } catch (IOException e) {
            throw new IllegalArgumentException(path + "에 해당하는 파일이 없습니다.", e);
        }
    }
}
