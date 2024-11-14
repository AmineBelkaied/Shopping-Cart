package com.example.shoppingcart.services.impl;

import com.example.shoppingcart.entities.Packet;
import com.example.shoppingcart.entities.PacketItem;
import com.example.shoppingcart.entities.Product;
import com.example.shoppingcart.enums.Status;
import com.example.shoppingcart.repositories.IPacketItemRepository;
import com.example.shoppingcart.repositories.IPacketRepository;
import com.example.shoppingcart.repositories.IProductRepository;
import com.example.shoppingcart.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PacketServiceImpl implements PacketService {

    private final IPacketRepository packetRepository;
    private final IPacketItemRepository packetItemRepository;
    private final IProductRepository productRepository;

    @Value("cancellation.packet.time.limit")
    private String packetTimeLimit;

    @Autowired
    public PacketServiceImpl(IPacketRepository packetRepository, IPacketItemRepository packetItemRepository, IProductRepository productRepository) {
        this.packetRepository = packetRepository;
        this.packetItemRepository = packetItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Packet> getAllPackets() {
        return packetRepository.findAll();
    }

    @Override
    public Packet getPacketById(Long id) {
        return packetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Packet not found with id: " + id));

    }

    @Override
    public List<Packet> getPacketsByCustomerId(Long customerId) {
        return packetRepository.findPacketByCustomerId(customerId);
    }

    @Override
    public Packet createPacket(Packet packet) {
        return packetRepository.save(packet);
    }

    @Override
    public Packet updatePacket(Long id, Packet packetDetails) {
        Packet packet = getPacketById(id);

        packet.setUpdateDate(packetDetails.getUpdateDate());
        packet.setDescription(packetDetails.getDescription());
        packet.setStatus(packetDetails.getStatus());
        packet.setCustomer(packetDetails.getCustomer());
        packet.setPacketItems(packetDetails.getPacketItems());

        return packetRepository.save(packet);
    }

    @Override
    public void addProductToShoppingCart(Packet packet, Product product, int quantity) throws Exception {
        if(product.getAvailableStock() - quantity < 0) {
            throw new Exception("No stock available for the product : " + product.getName());
        }
        packetItemRepository.save(new PacketItem(packet, product, quantity));
        product.setTemporaryStock(product.getTemporaryStock() - quantity);
    }

    @Override
    public void deleteProductFromShoppingCart(Long packetId, Product product) {
        PacketItem packetItem = packetItemRepository.findPacketItemByPacketIdAndProductId(packetId, product.getId());
        product.setTemporaryStock(product.getTemporaryStock() + packetItem.getQuantity());
        packetItemRepository.delete(packetItem);
    }

    @Override
    public void validatePacket(Packet packet) {
        packet.setStatus(Status.CONFIRMED);
        packet.setUpdateDate(new Date());
        packetRepository.save(packet);
        reduceProductQuantities(packet.getId());
    }

    private void reduceProductQuantities(Long packetId) {
        productRepository.reduceProductQuantities(packetId);
    }

    @Override
    public void cancelPacket(Packet packet, Long maxCancellationTimeMs) throws Exception {
        validateCancellationPeriod(packet, maxCancellationTimeMs);
        validatePacketStatus(packet);

        packet.setStatus(Status.CANCELED);
        packet.setUpdateDate(new Date());
        packetRepository.save(packet);

        restoreProductQuantities(packet.getId());
    }

    private void validateCancellationPeriod(Packet packet, Long maxCancellationTimeMs) throws Exception {
        long now = System.currentTimeMillis();
        long packetCreationTime = packet.getCreationDate().getTime();
        if (now - packetCreationTime > maxCancellationTimeMs) {
            throw new Exception("Cancellation period exceeded. You can only cancel within " +
                    (maxCancellationTimeMs / 60000) + " minutes of order creation.");
        }
    }

    private void validatePacketStatus(Packet packet) throws Exception {
        if (!Status.CONFIRMED.equals(packet.getStatus())) {
            throw new Exception("Cannot cancel packet: It must be confirmed before cancellation.");
        }
    }

    private void restoreProductQuantities(Long packetId) {
        productRepository.restoreProductQuantities(packetId);
    }

    @Override
    public void deletePacket(Long id) {
        packetRepository.deleteById(id);
    }
}
