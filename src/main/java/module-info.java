module org.example.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens org.example.finalproject to javafx.fxml;
    exports org.example.finalproject;
    exports org.example.finalproject.model;
    opens org.example.finalproject.model to javafx.fxml;
}