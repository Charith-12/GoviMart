package com.example.govimart;

import java.util.Date;

public class Notification {

    private String titleOfPost;
    private double quantityBought;
    private Date time;
    private String idOfPost;
    private String buyerName;
    private String buyerContactNumber;
    private String buyerAddress;
    private String buyerNote;
    private String message;
    private String buyerId;


    // Constructor
    public Notification(){
        // Empty one
    }

    public Notification(String titleOfPost, double quantityBought, Date time, String idOfPost, String buyerName, String buyerContactNumber, String buyerAddress, String buyerNote, String message, String buyerId) {
        this.titleOfPost = titleOfPost;
        this.quantityBought = quantityBought;
        this.time = time;
        this.idOfPost = idOfPost;
        this.buyerName = buyerName;
        this.buyerContactNumber = buyerContactNumber;
        this.buyerAddress = buyerAddress;
        this.buyerNote = buyerNote;
        this.message = message;
        this.buyerId = buyerId;
    }

    // Getters
    public String getTitleOfPost() {
        return titleOfPost;
    }

    public double getQuantityBought() {
        return quantityBought;
    }

    public Date getTime() {
        return time;
    }

    public String getIdOfPost() {
        return idOfPost;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerContactNumber() {
        return buyerContactNumber;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public String getBuyerNote() {
        return buyerNote;
    }

    public String getMessage() {
        return message;
    }

    public String getBuyerId() {
        return buyerId;
    }

    // Setters
    public void setTitleOfPost(String titleOfPost) {
        this.titleOfPost = titleOfPost;
    }

    public void setQuantityBought(double quantityBought) {
        this.quantityBought = quantityBought;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setIdOfPost(String idOfPost) {
        this.idOfPost = idOfPost;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public void setBuyerContactNumber(String buyerContactNumber) {
        this.buyerContactNumber = buyerContactNumber;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public void setBuyerNote(String buyerNote) {
        this.buyerNote = buyerNote;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}
