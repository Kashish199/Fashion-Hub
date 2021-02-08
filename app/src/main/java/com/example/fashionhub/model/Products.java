package com.example.fashionhub.model;

import android.net.Uri;

public class Products {

    String productid;
    String productName;
    String productQty;
    String productPrice;
    String description;
    private Uri uri;
    String category;
    Uri detail_image;

//

    public Products(String productid, String productName, String productQty, String productPrice, Uri uri, String description, String category, Uri detail_image) {
        this.productid = productid;
        this.productName = productName;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.uri = uri;
        this.description = description;
        this.category = category;
        this.detail_image = detail_image;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getProductDescription() {
        return description;
    }

    public void setProductDescription(String description) {
        this.description = description;
    }

    public Uri getImageUrl() {
        return uri;
    }

    public void setImageUrl(Uri imageUrl) {
        this.uri = uri;
    }

    public String getProductCategory() {
        return category;
    }

    public void setProductCategory(String category) {
        this.category = category;
    }

    public Uri getDetailmageUrl() {
        return detail_image;
    }

    public void setDetailimageUrl(Uri imageUrl) {
        this.detail_image = detail_image;
    }

    //    public Products(String productid, String productName, String productQty, String productPrice, Uri uri, String description, String category, Uri detail_image) {
//
//        this.productid = productid;
//        this.productName = productName;
//        this.productQty = productQty;
//        this.productPrice = productPrice;
//        this.uri = uri;
//        this.description = description;
//        this.category = category;
//        this.detail_image = detail_image;
//    }
//
//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//
//    public String getProductDescription() {
//        return description;
//    }
//
//    public void setProductDescription(String description) {
//        this.description = description;
//    }
//
//    public String getProductCategory() {
//        return category;
//    }
//
//    public void setProductCategory(String category) {
//        this.category = category;
//    }
//
//    public String getProductQty() {
//        return productQty;
//    }
//
//    public void setProductQty(String productQty) {
//        this.productQty = productQty;
//    }
//
//    public String getProductPrice() {
//        return productPrice;
//    }
//
//    public void setProductPrice(String productPrice) {
//        this.productPrice = productPrice;
//    }
//
//    public Uri getImageUrl() {
//        return uri;
//    }
//
//    public void setImageUrl(Uri imageUrl) {
//        this.uri = uri;
//    }
//
//    public Uri getDetailmageUrl() {
//        return detail_image;
//    }
//
//    public void setDetailimageUrl(Uri imageUrl) {
//        this.detail_image = detail_image;
//    }
//
//    public Integer getProductid() {
//        return productid;
//    }
//
//    public void setProductid(Integer productid) {
//        this.productid = productid;
//    }
}
