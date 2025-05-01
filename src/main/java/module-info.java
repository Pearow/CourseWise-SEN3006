module com.sen3006.coursewise {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.sen3006.coursewise.client to javafx.fxml, com.google.gson;
    opens com.sen3006.coursewise.client.models to com.google.gson;
    opens com.sen3006.coursewise.client.enums to com.google.gson;
    exports com.sen3006.coursewise.client;
}