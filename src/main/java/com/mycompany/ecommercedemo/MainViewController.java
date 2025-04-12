package com.mycompany.ecommercedemo;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

    @FXML
    private VBox productContainer;
    
    @FXML
    private Button viewCartButton;
    
    @FXML
    private Button checkoutButton;
    
    private List<Product> products = new ArrayList<>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize simple products
        products.add(new Product(1, "Laptop", 999.99, "Powerful laptop"));
        products.add(new Product(2, "Phone", 499.99, "Smartphone"));
        
        // Display products as simple labels
        for (Product product : products) {
            javafx.scene.control.Label label = new javafx.scene.control.Label(
                product.getName() + " - $" + product.getPrice());
            
            javafx.scene.control.Button addButton = new javafx.scene.control.Button("Add to Cart");
            addButton.setOnAction(e -> showAlert("Added", "Added " + product.getName() + " to cart!"));
            
            javafx.scene.layout.HBox productBox = new javafx.scene.layout.HBox(10, label, addButton);
            productBox.setPadding(new javafx.geometry.Insets(5));
            
            productContainer.getChildren().add(productBox);
        }
    }
    
    @FXML
    private void viewCartButtonAction(ActionEvent event) {
        // Switch to cart tab
        TabPane tabPane = (TabPane) productContainer.getParent().getParent().getParent();
        tabPane.getSelectionModel().select(1); // Select cart tab (index 1)
    }
    
    @FXML
    private void checkoutButtonAction(ActionEvent event) {
        showAlert("Checkout", "Proceeding to checkout!");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}