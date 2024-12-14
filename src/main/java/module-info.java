module com.greymatcha.todolist {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens com.greymatcha.todolist to javafx.fxml;
    opens com.greymatcha.todolist.fxmlcontroller;

    exports com.greymatcha.todolist;
    exports com.greymatcha.todolist.fxmlcontroller;
}