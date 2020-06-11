module md.jgames.jchess {
    requires java.base;
    requires java.desktop;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;

    requires mdlib.materifx;
    requires mdlib.utils;

    exports md.jgames.jchess to javafx.fxml, javafx.graphics;
    opens md.jgames.jchess.activities to javafx.fxml;
    opens md.jgames.jchess.components to javafx.fxml;
}