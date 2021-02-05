package com.example.fashionhub.model;

public class ApproveModel {
    String id;
    String name;
    String company;


    public ApproveModel(String id, String name, String company) {
        this.id = id;
        this.name = name;
        this.company = company;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }
}
