package com.example.shoppingcart.services;

import com.example.shoppingcart.entities.Customer;
import com.example.shoppingcart.entities.Packet;
import com.example.shoppingcart.entities.Product;

import java.util.List;
import java.util.Optional;

public interface PacketService {
    List<Packet> getAllPackets();
    Packet getPacketById(Long id);
    List<Packet> getPacketsByCustomerId(Long customerId);
    Packet createPacket(Packet packet);
    Packet updatePacket(Long id, Packet packet);

    void addProductToShoppingCart(Packet packet, Product product, int quantity) throws Exception;

    void deleteProductFromShoppingCart(Long packetId, Product product);

    void validatePacket(Packet packet);

    void cancelPacket(Packet packet, Long packetTimeLimit) throws Exception;

    void deletePacket(Long id);
}
