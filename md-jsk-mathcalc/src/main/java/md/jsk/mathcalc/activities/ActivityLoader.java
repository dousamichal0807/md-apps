package md.jsk.mathcalc.activities;

import java.io.FileNotFoundException;
import java.net.URL;

public final class ActivityLoader {

    private ActivityLoader() {
    }

    public static URL loadActivity(String activityName) {
        URL url = ActivityLoader.class.getResource("activity_" + activityName + ".fxml");
        if (url == null)
            throw new RuntimeException(new FileNotFoundException("Activity FXML file not found: \"" + activityName + "\""));
        return url;
    }
}
