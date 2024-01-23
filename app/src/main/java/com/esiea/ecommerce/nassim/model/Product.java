package com.esiea.ecommerce.nassim.model;

import java.util.List;

public class Product {
    private int id;
    private String title;
    private int price;
    private List<String> images;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Product(int id, String title, int price, List<String> imageUrl) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.images = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
