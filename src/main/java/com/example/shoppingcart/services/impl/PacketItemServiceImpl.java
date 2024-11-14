package com.example.shoppingcart.services.impl;

import com.example.shoppingcart.entities.PacketItem;
import com.example.shoppingcart.repositories.IPacketItemRepository;
import com.example.shoppingcart.services.PacketItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacketItemServiceImpl implements PacketItemService {

    private final IPacketItemRepository packetItemRepository;

    @Autowired
    public PacketItemServiceImpl(IPacketItemRepository packetItemRepository) {
        this.packetItemRepository = packetItemRepository;
    }

    @Override
    public List<PacketItem> getAllPacketItems() {
        return packetItemRepository.findAll();
    }

    @Override
    public List<PacketItem> getAllPacketItemsByPacketId(Long packetId) {
        return packetItemRepository.findPacketItemByPacketId(packetId);
    }

    @Override
    public Optional<PacketItem> getPacketItemById(Long id) {
        return packetItemRepository.findById(id);
    }

    @Override
    public PacketItem createPacketItem(PacketItem packetItem) {
        return packetItemRepository.save(packetItem);
    }

    @Override
    public PacketItem updatePacketItem(Long id, PacketItem packetDetails) {
        PacketItem packetItem = packetItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PacketItem not found with id: " + id));

        packetItem.setPacket(packetDetails.getPacket());
        packetItem.setProduct(packetDetails.getProduct());
        packetItem.setQuantity(packetDetails.getQuantity());

        return packetItemRepository.save(packetItem);
    }

    @Override
    public void deletePacketItem(Long id) {
        packetItemRepository.deleteById(id);
    }
}
