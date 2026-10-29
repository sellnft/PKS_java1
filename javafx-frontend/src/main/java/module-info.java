module com.example.javafxfrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.dlsc.fxmlkit;
    requires java.logging;
    requires java.desktop;

    requires retrofit2;
    requires retrofit2.converter.jackson;
    requires okhttp3;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.apache.poi.ooxml;


    opens javafxfrontend to javafx.fxml;
    opens javafxfrontend.controller to javafx.fxml;
    opens javafxfrontend.dto to com.fasterxml.jackson.databind;

    exports javafxfrontend;
}