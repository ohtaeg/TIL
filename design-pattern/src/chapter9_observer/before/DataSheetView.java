package chapter9_observer.before;

import java.util.List;

public class DataSheetView {
    private ScoreRecord scoreRecord;
    private int viewCount;

    public DataSheetView(final ScoreRecord scoreRecord, final int viewCount) {
        this.scoreRecord = scoreRecord;
        this.viewCount = viewCount;
    }

    public void update() {
        List<Integer> record = scoreRecord.getScores();
        display(record, viewCount);
    }

    private void display(final List<Integer> record, final int viewCount) {
        System.out.println("List of " + viewCount + ", entries : ");
        for (int i = 0; i < viewCount && i < record.size(); i++) {
            System.out.print(record.get(i) + " ");
        }
        System.out.println();
    }
}
