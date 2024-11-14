package com.example.shoppingcart.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "packet_item")
public class PacketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "packet_id")
    private Packet packet;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public PacketItem() {}

    public PacketItem(Packet packet, Product product, int quantity) {
        this.packet = packet;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "PacketItem{" +
                "id=" + id +
                ", packet=" + packet +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}