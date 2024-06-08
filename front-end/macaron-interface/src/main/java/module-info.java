module fr.clementjaminion.frontendinterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.web;
    requires spring.boot.starter.data.jpa;
    requires spring.context;
    requires spring.data.jpa;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires jakarta.annotation;
    requires spring.webmvc;


    opens fr.clementjaminion.frontendinterface to javafx.fxml;
    exports fr.clementjaminion.frontendinterface;
    exports fr.clementjaminion.macaronsbackend.models;
}
