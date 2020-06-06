module md.jgames.jchess {
    requires java.base;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires mdlib.materij;
    requires mdlib.utils;
    exports md.jgames.jchess;
    opens md.jgames.jchess.activities;
}