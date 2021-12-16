package chapter9_observer.before;

import java.util.Collections;
import java.util.List;

public class MinMaxView {
    private ScoreRecord scoreRecord;

    public MinMaxView(final ScoreRecord scoreRecord) {
        this.scoreRecord = scoreRecord;
    }

    public void update() {
        List<Integer> record = scoreRecord.getScores();
        displayMinMax(record);
    }

    private void displayMinMax(final List<Integer> record) {
        int min = Collections.min(record);
        int max = Collections.max(record);
        System.out.println("min : " + min + ", max : " + max);
    }
}
