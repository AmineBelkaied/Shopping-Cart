package com.example.shoppingcart.repositories;

import com.example.shoppingcart.entities.Packet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPacketRepository extends JpaRepository<Packet, Long> {
    List<Packet> findPacketByCustomerId(Long customerId);
}
