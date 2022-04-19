package chapter9_observer.before;

import java.util.ArrayList;
import java.util.List;

public class ScoreRecord {
    private List<Integer> scores = new ArrayList<>();
    private List<DataSheetView> dataSheetViews = new ArrayList<>();
    private MinMaxView minMaxView;

    public void addDataSheetViews(final DataSheetView dataSheetViews) {
        this.dataSheetViews.add(dataSheetViews);
    }

    public void setMinMaxView(final MinMaxView minMaxView) {
        this.minMaxView = minMaxView;
    }

    public void addScore(int score) {
        scores.add(score);
        for (DataSheetView dataSheetView : dataSheetViews) {
            dataSheetView.update();
        }
        minMaxView.update();
    }

    public List<Integer> getScores() {
        return scores;
    }
}
