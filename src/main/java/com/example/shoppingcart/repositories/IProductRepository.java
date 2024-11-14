package com.example.shoppingcart.repositories;

import com.example.shoppingcart.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE product p JOIN packet_item pi ON pi.product_id = p.id SET p.available_stock = p.available_stock - pi.quantity WHERE pi.packet_id = :packetId", nativeQuery = true)
    void reduceProductQuantities(@Param("packetId") Long packetId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product p JOIN packet_item pi ON pi.product_id = p.id SET p.available_stock = p.available_stock + pi.quantity WHERE pi.packet_id = :packetId",
            nativeQuery = true)
    void restoreProductQuantities(@Param("packetId") Long packetId);

    @Query("SELECT p FROM Product p WHERE p.availableStock > 0")
    List<Product> findAvailableProducts();
}
