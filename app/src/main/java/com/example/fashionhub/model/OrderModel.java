package com.example.fashionhub.model;

import android.net.Uri;

public class OrderModel {

    String productName;
    String productQty;
    String productPrice;
    String flat;
    String postal;
    String city;

    String address;
    Uri detail_image;

    public OrderModel(String productName, String productQty, String productPrice, Uri uri, String flat, String postal, String city, String address) {

        this.productName = productName;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.flat = flat;
        this.postal = postal;
        this.city = city;
        this.address = address;
        this.detail_image = uri;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getaddress() {
        return address;
    }

    public void seraddress(String address) {
        this.address = address;
    }

    public String getcity() {
        return city;
    }

    public void setcity(String city) {
        this.city = city;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getpostal() {
        return postal;
    }

    public void setpostal(String postal) {
        this.postal = postal;
    }

    public Uri getDetailmageUrl() {
        return detail_image;
    }

    public void setDetailimageUrl(Uri imageUrl) {
        this.detail_image = imageUrl;
    }

    public String getflat() {
        return flat;
    }

    public void setflat(String flat) {
        this.flat = flat;
    }
}
