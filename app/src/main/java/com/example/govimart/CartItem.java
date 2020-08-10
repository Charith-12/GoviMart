package com.example.govimart;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class CartItem {

    private String cartItemPostId;
    private String cartItemOwnerId;
    private Date cartItemDateAdded;
    private String cartItemPostTitle;
    private String cartItemCategory;
    private double cartItemQuantity;
    private String cartItemUnitPrice;
    private double cartItemTotalPrice;
    private String cartItemDistrict;
    private String cartItemLocation;


    public CartItem(){

    }

    public CartItem(String cartItemPostId, String cartItemOwnerId, Date cartItemDateAdded, String cartItemPostTitle, String cartItemCategory, double cartItemQuantity, String cartItemUnitPrice, double cartItemTotalPrice, String cartItemDistrict, String cartItemLocation) {
        this.cartItemPostId = cartItemPostId;
        this.cartItemOwnerId = cartItemOwnerId;
        this.cartItemDateAdded = cartItemDateAdded;
        this.cartItemPostTitle = cartItemPostTitle;
        this.cartItemCategory = cartItemCategory;
        this.cartItemQuantity = cartItemQuantity;
        this.cartItemUnitPrice = cartItemUnitPrice;
        this.cartItemTotalPrice = cartItemTotalPrice;
        this.cartItemDistrict = cartItemDistrict;
        this.cartItemLocation = cartItemLocation;
    }

    // Getters
    public String getCartItemPostId() {
        return cartItemPostId;
    }

    public String getCartItemOwnerId() {
        return cartItemOwnerId;
    }

    public Date getCartItemDateAdded() {
        return cartItemDateAdded;
    }

    public String getCartItemPostTitle() {
        return cartItemPostTitle;
    }

    public String getCartItemCategory() {
        return cartItemCategory;
    }

    public double getCartItemQuantity() {
        return cartItemQuantity;
    }

    public String getCartItemUnitPrice() {
        return cartItemUnitPrice;
    }

    public double getCartItemTotalPrice() {
        return cartItemTotalPrice;
    }

    public String getCartItemDistrict() {
        return cartItemDistrict;
    }

    public String getCartItemLocation() {
        return cartItemLocation;
    }

    // Setters


    public void setCartItemPostId(String cartItemPostId) {
        this.cartItemPostId = cartItemPostId;
    }

    public void setCartItemOwnerId(String cartItemOwnerId) {
        this.cartItemOwnerId = cartItemOwnerId;
    }

    public void setCartItemDateAdded(Date cartItemDateAdded) {
        this.cartItemDateAdded = cartItemDateAdded;
    }

    public void setCartItemPostTitle(String cartItemPostTitle) {
        this.cartItemPostTitle = cartItemPostTitle;
    }

    public void setCartItemCategory(String cartItemCategory) {
        this.cartItemCategory = cartItemCategory;
    }

    public void setCartItemQuantity(double cartItemQuantity) {
        this.cartItemQuantity = cartItemQuantity;
    }

    public void setCartItemUnitPrice(String cartItemUnitPrice) {
        this.cartItemUnitPrice = cartItemUnitPrice;
    }

    public void setCartItemTotalPrice(double cartItemTotalPrice) {
        this.cartItemTotalPrice = cartItemTotalPrice;
    }

    public void setCartItemDistrict(String cartItemDistrict) {
        this.cartItemDistrict = cartItemDistrict;
    }

    public void setCartItemLocation(String cartItemLocation) {
        this.cartItemLocation = cartItemLocation;
    }
}
