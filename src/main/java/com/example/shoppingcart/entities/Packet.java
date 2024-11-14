package com.example.shoppingcart.entities;

import com.example.shoppingcart.enums.Status;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Packet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "update_date")
    private Date updateDate;
    private String description;
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "packet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PacketItem> packetItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Packet() {}

    public Packet(Customer customer, Status status, String description, Date updateDate, Date creationDate) {
        this.customer = customer;
        this.status = status;
        this.description = description;
        this.updateDate = updateDate;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<PacketItem> getPacketItems() {
        return packetItems;
    }

    public void setPacketItems(List<PacketItem> packetItems) {
        this.packetItems = packetItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Packet(Long id, Date creationDate, Date updateDate, String description, Status status, Customer customer) {
        this.id = id;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.description = description;
        this.status = status;
        this.customer = customer;
    }
}
