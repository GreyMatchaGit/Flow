module com.greymatcha.flow {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;

    opens com.greymatcha.flow to javafx.fxml;
    opens com.greymatcha.flow.fxmlcontroller;
    opens com.greymatcha.flow.models;
    opens com.greymatcha.flow.utils;

    exports com.greymatcha.flow;
    exports com.greymatcha.flow.models;
    exports com.greymatcha.flow.fxmlcontroller;
}