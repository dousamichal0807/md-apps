module md.jgames.jchess {
    requires java.base;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires mdlib.materij;
    requires mdlib.utils;
    requires java.desktop;

    exports md.jgames.jchess to javafx.fxml, javafx.graphics;
    opens md.jgames.jchess.activities to javafx.fxml;
}