package com.example.fashionhub.model;

public class ApproveModel {
    String id;
    String name;
    String company;
    String AdminApprove;

    public ApproveModel(String id, String name, String company, String adminApprove) {
        this.id = id;
        this.name = name;
        this.company = company;
        AdminApprove = adminApprove;
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

    public String getAdminApprove() {
        return AdminApprove;
    }
}
