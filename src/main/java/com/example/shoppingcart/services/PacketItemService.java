package com.example.shoppingcart.services;

import com.example.shoppingcart.entities.PacketItem;

import java.util.List;
import java.util.Optional;

public interface PacketItemService {
    List<PacketItem> getAllPacketItems();
    List<PacketItem> getAllPacketItemsByPacketId(Long packetId);
    Optional<PacketItem> getPacketItemById(Long id);
    PacketItem createPacketItem(PacketItem packetItem);
    PacketItem updatePacketItem(Long id, PacketItem packetItem);
    void deletePacketItem(Long id);
}
