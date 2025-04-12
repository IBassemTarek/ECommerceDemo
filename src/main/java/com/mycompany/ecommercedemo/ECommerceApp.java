package com.mycompany.ecommercedemo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ECommerceApp extends Application {
    
    private List<Product> products = new ArrayList<>();
    private Map<Product, Integer> cartItems = new HashMap<>();
    private ObservableList<String> cartListItems = FXCollections.observableArrayList();
    private Label totalPriceLabel = new Label("$0.00");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    
    @Override
    public void start(Stage stage) {
        // Initialize products
        products.add(new Product(1, "Laptop", 999.99, "Powerful laptop with latest features"));
        products.add(new Product(2, "Smartphone", 699.99, "Latest smartphone model"));
        products.add(new Product(3, "Headphones", 149.99, "Wireless noise-cancelling headphones"));
        products.add(new Product(4, "Tablet", 349.99, "High-resolution tablet"));
        
        // Create header
        Label titleLabel = new Label("Simple E-Commerce Demo");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);
        
        HBox header = new HBox(titleLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #3498db;");
        
        // Create product container
        VBox productsBox = new VBox(10);
        productsBox.setPadding(new Insets(15));
        
        // Add product items
        for (Product product : products) {
            HBox productBox = createProductBox(product);
            productsBox.getChildren().add(productBox);
        }
        
        // Create cart container
        VBox cartBox = new VBox(10);
        cartBox.setPadding(new Insets(15));
        
        // Create cart list view
        ListView<String> cartListView = new ListView<>(cartListItems);
        cartListView.setPrefHeight(300);
        
        // Create cart total
        HBox totalBox = new HBox(10);
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        Label totalLabel = new Label("Total:");
        totalLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        totalPriceLabel.setFont(Font.font("System", 16));
        totalBox.getChildren().addAll(totalLabel, totalPriceLabel);
        
        // Create cart buttons
        HBox cartButtonsBox = new HBox(10);
        cartButtonsBox.setAlignment(Pos.CENTER);
        Button clearCartButton = new Button("Clear Cart");
        clearCartButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        clearCartButton.setOnAction(e -> clearCart());
        
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        checkoutButton.setOnAction(e -> {
            if (cartItems.isEmpty()) {
                showAlert(stage, "Empty Cart", "Your cart is empty. Add some products before checkout.");
            } else {
                showAlert(stage, "Checkout", "Thank you for your order!\nTotal: " + totalPriceLabel.getText());
                clearCart();
            }
        });
        
        cartButtonsBox.getChildren().addAll(clearCartButton, checkoutButton);
        
        // Add all elements to the cart box
        cartBox.getChildren().addAll(
            new Label("Your Cart"), 
            cartListView, 
            totalBox, 
            cartButtonsBox
        );
        
        // Create tabs
        TabPane tabPane = new TabPane();
        
        Tab productsTab = new Tab("Products");
        productsTab.setContent(productsBox);
        productsTab.setClosable(false);
        
        Tab cartTab = new Tab("Cart");
        cartTab.setContent(cartBox);
        cartTab.setClosable(false);
        
        tabPane.getTabs().addAll(productsTab, cartTab);
        
        // Create main layout
        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(tabPane);
        
        // Create scene and show stage
        Scene scene = new Scene(root, 600, 500);
        stage.setTitle("E-Commerce Demo");
        stage.setScene(scene);
        stage.show();
    }
    
    private HBox createProductBox(Product product) {
        Label nameLabel = new Label(product.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        Label priceLabel = new Label(currencyFormat.format(product.getPrice()));
        
        Label descLabel = new Label(product.getDescription());
        
        VBox infoBox = new VBox(5, nameLabel, priceLabel, descLabel);
        
        Button addButton = new Button("Add to Cart");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            addToCart(product);
            showAlert(null, "Added to Cart", 
                    "Added " + product.getName() + " to your cart!");
        });
        
        HBox productBox = new HBox(15, infoBox, addButton);
        productBox.setAlignment(Pos.CENTER_LEFT);
        productBox.setPadding(new Insets(10));
        productBox.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5;");
        
        return productBox;
    }
    
    private void addToCart(Product product) {
        // Update cart items
        int quantity = cartItems.getOrDefault(product, 0);
        cartItems.put(product, quantity + 1);
        
        // Update UI
        updateCartList();
        updateTotalPrice();
    }
    
    private void clearCart() {
        cartItems.clear();
        updateCartList();
        updateTotalPrice();
    }
    
    private void updateCartList() {
        cartListItems.clear();
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double subtotal = product.getPrice() * quantity;
            
            String itemText = quantity + "x " + product.getName() + " - " + 
                             currencyFormat.format(subtotal);
            cartListItems.add(itemText);
        }
    }
    
    private void updateTotalPrice() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        totalPriceLabel.setText(currencyFormat.format(total));
    }
    
    private void showAlert(Stage owner, String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}