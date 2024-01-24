package com.esiea.ecommerce.nassim.model;

public class ProductUpdateRequest {
    private String title;
    private int price;
    private String description;

    public ProductUpdateRequest(String title, int price, String description) {
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public ProductUpdateRequest(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
