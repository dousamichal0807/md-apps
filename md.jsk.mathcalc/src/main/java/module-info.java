module md.jsk.mathcalc {
    requires java.base;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires mdlib.utils;
    requires mdlib.jmath;
    requires mdlib.mdfx;

    exports md.jsk.mathcalc to javafx.fxml, javafx.graphics;
    opens md.jsk.mathcalc.activities to javafx.fxml;
}