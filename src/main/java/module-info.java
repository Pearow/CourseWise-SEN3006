module com.sen3006.coursewise {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.sen3006.coursewise to javafx.fxml, com.google.gson;
    exports com.sen3006.coursewise;
}