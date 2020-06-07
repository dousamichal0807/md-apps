package md.jsk.chemitables.activities;

import java.io.FileNotFoundException;
import java.net.URL;

public final class ActivityLoader {

    // Do not create any instances
    private ActivityLoader() {
    }

    /**
     * Returns an {@link URL} of given activity FXML file, if exists.
     *
     * @param activityName the name of the requested activity
     * @return {@link URL} of given activity FXML file
     * @throws RuntimeException caused by {@link FileNotFoundException} if activity
     *                          FXML file does not exist
     */
    public static URL loadActivityFXML(final String activityName) {
        URL url = ActivityLoader.class.getResource("activity_" + activityName + ".fxml");
        if (url == null)
            throw new RuntimeException(new FileNotFoundException("Activity FXML file not found: \"" + activityName + "\""));
        return url;
    }
}
