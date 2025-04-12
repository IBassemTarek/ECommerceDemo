module com.mycompany.ecommercedemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    
    opens com.mycompany.ecommercedemo to javafx.fxml;
    exports com.mycompany.ecommercedemo;
}