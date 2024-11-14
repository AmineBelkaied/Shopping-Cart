package com.example.shoppingcart.repositories;

import com.example.shoppingcart.entities.PacketItem;
import com.example.shoppingcart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPacketItemRepository extends JpaRepository<PacketItem, Long> {

    PacketItem findPacketItemByPacketIdAndProductId(long packetId, long productId);
    List<PacketItem> findPacketItemByPacketId(long packetId);
}
