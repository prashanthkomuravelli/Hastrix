package com.prashanth.hastrix;

/**
 * Created by Satyam on 30-03-2017.
 */

public class SingleProductDetails {
    private String productName;
    private String quantityMsg;
    private int quantity;
    private float price;

    public SingleProductDetails(String productName, String quantityMsg, int quantity) {
        this.productName = productName;
        this.quantityMsg = quantityMsg;
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantityMsg() {
        return quantityMsg;
    }

    public void setQuantityMsg(String quantityMsg) {
        this.quantityMsg = quantityMsg;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
