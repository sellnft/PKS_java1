module com.example.javafxfrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.dlsc.fxmlkit;
    requires java.logging;
    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires java.desktop;

    opens javafxfrontend to javafx.fxml;
    exports javafxfrontend;
}