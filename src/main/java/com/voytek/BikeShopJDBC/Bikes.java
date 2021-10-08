package com.voytek.BikeShopJDBC;


public class Bikes {

    private long id;
    private String name;
    private String description;
    private int price;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Bikes(long idbikes, String name, String description, int price) {
        this.id = idbikes;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Bikes() {
    }

    @Override
    public String toString() {
        return "Bikes{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
