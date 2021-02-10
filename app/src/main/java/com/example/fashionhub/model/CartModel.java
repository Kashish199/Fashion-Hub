package com.example.fashionhub.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class CartModel implements Parcelable {
    String id;
    String productid;
    String productName;
    String productQty;
    String productPrice;
    private Uri uri;


    public CartModel(String id, String productid, String productName, String productQty, String productPrice, Uri uri) {
        this.id = id;
        this.productid = productid;
        this.productName = productName;
        this.productQty = productQty;
        this.productPrice = productPrice;
        this.uri = uri;

    }

    protected CartModel(Parcel in) {

        id = in.readString();
        productid = in.readString();

        productName = in.readString();
        productQty = in.readString();
        productPrice = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Parcelable.Creator<CartModel> CREATOR = new Parcelable.Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public String getId() {
        return id;
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

    public Uri getImageUrl() {
        return uri;
    }

    public void setImageUrl(Uri imageUrl) {
        this.uri = uri;
    }


    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(productid);
        parcel.writeString(productName);
        parcel.writeString(productQty);
        parcel.writeString(productPrice);
        parcel.writeParcelable(uri, i);
    }
}
