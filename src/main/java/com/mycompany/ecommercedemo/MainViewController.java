package com.mycompany.ecommercedemo;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {

    @FXML
    private FlowPane productContainer;
    
    @FXML
    private ListView<String> cartListView;
    
    @FXML
    private Label totalPriceLabel;
    
    @FXML
    private Label checkoutTotalLabel;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField cityField;
    
    @FXML
    private TextField postalCodeField;
    
    @FXML
    private TextField phoneField;
    
    private List<Product> products = new ArrayList<>();
    private ShoppingCart cart = new ShoppingCart();
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize sample products
        initializeProducts();
        
        // Display products
        displayProducts();
        
        // Bind cart total to labels
        cart.totalPriceProperty().addListener((obs, oldVal, newVal) -> {
            totalPriceLabel.setText(currencyFormat.format(newVal));
            checkoutTotalLabel.setText("Total: " + currencyFormat.format(newVal));
        });
        
        // Bind cart items to list view
        cartListView.getItems().clear();
        cart.getCartItems().addListener((javafx.collections.ListChangeListener.Change<? extends ShoppingCart.CartItem> c) -> {
            updateCartListView();
        });
    }
    
    private void initializeProducts() {
        // Add sample products
        products.add(new Product(1, "Laptop", 999.99, "Powerful laptop with the latest processor", "/images/laptop.png"));
        products.add(new Product(2, "Smartphone", 699.99, "Latest smartphone with high resolution camera", "/images/smartphone.png"));
        products.add(new Product(3, "Headphones", 149.99, "Noise cancelling wireless headphones", "/images/headphones.png"));
        products.add(new Product(4, "Smartwatch", 249.99, "Fitness tracker with heart rate monitor", "/images/smartwatch.png"));
        products.add(new Product(5, "Tablet", 399.99, "Lightweight tablet with high resolution display", "/images/tablet.png"));
        products.add(new Product(6, "Bluetooth Speaker", 79.99, "Portable wireless speaker with great sound", "/images/speaker.png"));
    }
    
    private void displayProducts() {
        productContainer.getChildren().clear();
        
        for (Product product : products) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProductItem.fxml"));
                VBox productBox = loader.load();
                
                // Set product details
                ((Label) productBox.lookup("#productNameLabel")).setText(product.getName());
                ((Label) productBox.lookup("#productPriceLabel")).setText(currencyFormat.format(product.getPrice()));
                ((Label) productBox.lookup("#productDescriptionLabel")).setText(product.getDescription());
                
                // For this demo, we'll use a placeholder image
                //((ImageView) productBox.lookup("#productImage")).setImage(product.getImage());
                
                // Add to cart button action
                Button addToCartButton = (Button) productBox.lookup("#addToCartButton");
                addToCartButton.setOnAction(event -> {
                    cart.addProduct(product);
                    showAlert("Product Added", "Added " + product.getName() + " to cart");
                });
                
                productContainer.getChildren().add(productBox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void updateCartListView() {
        cartListView.getItems().clear();
        for (ShoppingCart.CartItem item : cart.getCartItems()) {
            String cartItemText = item.getQuantity() + "x " + item.getProduct().getName() + 
                                " - " + currencyFormat.format(item.getSubtotal());
            cartListView.getItems().add(cartItemText);
        }
    }
    
    @FXML
    private void viewCartButtonAction(ActionEvent event) {
        // Switch to cart tab
        Node node = (Node) event.getSource();
        TabPane tabPane = (TabPane) node.getParent().getParent().getParent().getParent();
        tabPane.getSelectionModel().select(1); // Select cart tab (index 1)
    }
    
    @FXML
    private void clearCartButtonAction(ActionEvent event) {
        cart.clear();
    }
    
    @FXML
    private void checkoutButtonAction(ActionEvent event) {
        if (cart.getCartItems().isEmpty()) {
            showAlert("Empty Cart", "Your cart is empty. Add some products before checkout.");
            return;
        }
        
        // Switch to checkout tab
        Node node = (Node) event.getSource();
        TabPane tabPane = (TabPane) node.getParent().getParent().getParent().getParent();
        tabPane.getSelectionModel().select(2); // Select checkout tab (index 2)
    }
    
    @FXML
    private void placeOrderButtonAction(ActionEvent event) {
        if (cart.getCartItems().isEmpty()) {
            showAlert("Empty Cart", "Your cart is empty. Add some products before placing an order.");
            return;
        }
        
        // Validate form
        if (nameField.getText().isEmpty() || 
            emailField.getText().isEmpty() || 
            addressField.getText().isEmpty() || 
            cityField.getText().isEmpty() || 
            postalCodeField.getText().isEmpty() || 
            phoneField.getText().isEmpty()) {
            
            showAlert("Missing Information", "Please fill out all fields before placing your order.");
            return;
        }
        
        // Process the order (in a real app, this would save to database, etc.)
        showAlert("Order Placed", "Thank you for your order, " + nameField.getText() + "!\n\n" +
                "Your total: " + currencyFormat.format(cart.getTotalPrice()) + 
                "\nYour order will be shipped to: " + addressField.getText() + ", " + 
                cityField.getText() + " " + postalCodeField.getText());
        
        // Clear the cart and form
        cart.clear();
        clearForm();
        
        // Switch back to shop tab
        Node node = (Node) event.getSource();
        TabPane tabPane = (TabPane) node.getParent().getParent().getParent().getParent().getParent();
        tabPane.getSelectionModel().select(0); // Select shop tab (index 0)
    }
    
    private void clearForm() {
        nameField.clear();
        emailField.clear();
        addressField.clear();
        cityField.clear();
        postalCodeField.clear();
        phoneField.clear();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}