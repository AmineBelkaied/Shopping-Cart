package com.example.shoppingcart.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private String name;
    private String description;
    private double price;
    @Column(name = "available_stock")
    private int availableStock;

    @Transient
    private int temporaryStock;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PacketItem> packetItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {
    }

    public Product(String reference, String name, String description, double price, int availableStock, int temporaryStock, Category category) {
        this.reference = reference;
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableStock = availableStock;
        this.temporaryStock = temporaryStock;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public int getTemporaryStock() {
        return temporaryStock;
    }

    public void setTemporaryStock(int temporaryStock) {
        this.temporaryStock = temporaryStock;
    }

    public List<PacketItem> getPacketItems() {
        return packetItems;
    }

    public void setPacketItems(List<PacketItem> packetItems) {
        this.packetItems = packetItems;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", availableStock=" + availableStock +
                ", category=" + category +
                '}';
    }
}
