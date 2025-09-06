module com.example.labo4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.labo4 to javafx.fxml;
    exports com.example.labo4;
}