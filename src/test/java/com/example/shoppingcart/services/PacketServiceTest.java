package com.example.shoppingcart.services;

import com.example.shoppingcart.entities.Customer;
import com.example.shoppingcart.entities.Packet;
import com.example.shoppingcart.entities.Product;
import com.example.shoppingcart.enums.Status;
import com.example.shoppingcart.repositories.IPacketRepository;
import com.example.shoppingcart.repositories.IProductRepository;
import com.example.shoppingcart.services.impl.PacketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PacketServiceTest {

    @Mock
    private IPacketRepository packetRepository;

    @Mock
    private IProductRepository productRepository;

    @InjectMocks
    private PacketServiceImpl packetService;

    private Packet packet;
    private Product product;
    private Customer customer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setName("Paul Bk");

        packet = new Packet();
        packet.setId(1L);
        packet.setCreationDate(new Date());
        packet.setStatus(Status.IN_PROGRESS);
        packet.setCustomer(customer);

        product = new Product();
        product.setId(1L);
        product.setName("FreshFit");
        product.setAvailableStock(20);
    }

    @Test
    public void testCreatePacket() {
        when(packetRepository.save(any(Packet.class))).thenReturn(packet);

        Packet createdPacket = packetService.createPacket(packet);

        assertEquals(Status.IN_PROGRESS, createdPacket.getStatus());
        verify(packetRepository, times(1)).save(packet);
    }

    @Test
    public void testValidatePacket() {
        packet.setStatus(Status.IN_PROGRESS);
        when(packetRepository.save(any(Packet.class))).thenReturn(packet);

        packetService.validatePacket(packet);

        assertEquals(Status.CONFIRMED, packet.getStatus());
        verify(packetRepository, times(1)).save(packet);
    }

    @Test
    public void testCancelPacketWithinTimeLimit() throws Exception {
        packet.setStatus(Status.CONFIRMED);
        when(packetRepository.save(any(Packet.class))).thenReturn(packet);

        packetService.cancelPacket(packet, 1000L);

        assertEquals(Status.CANCELED, packet.getStatus());
        verify(packetRepository, times(1)).save(packet);
        verify(productRepository, times(1)).restoreProductQuantities(packet.getId());
    }

    @Test
    public void testCancelPacketBeyondTimeLimit() {
        packet.setStatus(Status.CONFIRMED);

        assertThrows(RuntimeException.class, () -> packetService.cancelPacket(packet, 0L));
    }
}
