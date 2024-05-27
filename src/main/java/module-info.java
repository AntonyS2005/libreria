module com.example.li {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.li to javafx.fxml;
    exports com.example.li;
    exports modelo;
    opens modelo to javafx.fxml;
}