package com.mycompany.ecommercedemo;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ShoppingCart {
    private final Map<Product, Integer> items = new HashMap<>();
    private final SimpleDoubleProperty totalPrice = new SimpleDoubleProperty(0);
    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();
    
    public void addProduct(Product product) {
        int quantity = items.getOrDefault(product, 0);
        items.put(product, quantity + 1);
        updateCartItems();
        calculateTotal();
    }
    
    public void removeProduct(Product product) {
        if (items.containsKey(product)) {
            int quantity = items.get(product);
            if (quantity > 1) {
                items.put(product, quantity - 1);
            } else {
                items.remove(product);
            }
            updateCartItems();
            calculateTotal();
        }
    }
    
    public void clear() {
        items.clear();
        updateCartItems();
        calculateTotal();
    }
    
    private void updateCartItems() {
        cartItems.clear();
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            cartItems.add(new CartItem(entry.getKey(), entry.getValue()));
        }
    }
    
    private void calculateTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        totalPrice.set(total);
    }
    
    public double getTotalPrice() {
        return totalPrice.get();
    }
    
    public SimpleDoubleProperty totalPriceProperty() {
        return totalPrice;
    }
    
    public ObservableList<CartItem> getCartItems() {
        return cartItems;
    }
    
    public static class CartItem {
        private final Product product;
        private final int quantity;
        
        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
        
        public Product getProduct() {
            return product;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public double getSubtotal() {
            return product.getPrice() * quantity;
        }
    }
}