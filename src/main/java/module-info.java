module com.sen3006.coursewise {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sen3006.coursewise to javafx.fxml;
    exports com.sen3006.coursewise;
}