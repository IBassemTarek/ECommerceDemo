package com.mycompany.ecommercedemo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import java.text.NumberFormat;

public class ProductCard extends VBox {
    
    private final Product product;
    
    public ProductCard(Product product, ShoppingCart cart) {
        this.product = product;
        
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10));
        setSpacing(8);
        setPrefWidth(180);
        setPrefHeight(220);
        setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-background-color: white;");
        
        // Product name
        Label nameLabel = new Label(product.getName());
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        nameLabel.setWrapText(true);
        nameLabel.setTextAlignment(TextAlignment.CENTER);
        nameLabel.setMaxWidth(160);
        
        // Product price
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        Label priceLabel = new Label(currencyFormat.format(product.getPrice()));
        priceLabel.setFont(Font.font("System", 14));
        
        // Product description
        Label descLabel = new Label(product.getDescription());
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(TextAlignment.CENTER);
        descLabel.setMaxWidth(160);
        descLabel.setMaxHeight(60);
        descLabel.setFont(Font.font("System", 11));
        
        // Add to cart button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        addToCartButton.setOnAction(e -> {
            cart.addProduct(product);
            showAddedToCartEffect();
        });
        
        getChildren().addAll(nameLabel, priceLabel, descLabel, addToCartButton);
    }
    
    private void showAddedToCartEffect() {
        // Flash effect when added to cart
        String originalStyle = getStyle();
        setStyle(originalStyle + "; -fx-background-color: #d4efdf;");
        
        // Reset after a short delay
        new Thread(() -> {
            try {
                Thread.sleep(300);
                javafx.application.Platform.runLater(() -> setStyle(originalStyle));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}