package com.example.fashionhub.model;

import android.net.Uri;

public class PostListModel {
    String productId;
    String price;
    String category;
    String title;
    String size;
    private Uri image;

    public PostListModel(String productId, String title, String price, String category, String size, Uri image) {
        this.productId = productId;
        this.price = price;
        this.category = category;
        this.image = image;
        this.size = size;
        this.title = title;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
