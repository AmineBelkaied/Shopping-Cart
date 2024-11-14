package com.example.shoppingcart.controllers;

import com.example.shoppingcart.entities.Packet;
import com.example.shoppingcart.enums.Status;
import com.example.shoppingcart.services.PacketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PacketControllerTest {

    @InjectMocks
    private PacketController packetController;

    @Mock
    private PacketService packetService;

    private Packet samplePacket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        samplePacket = new Packet();
        samplePacket.setId(1L);
        samplePacket.setStatus(Status.IN_PROGRESS);
        samplePacket.setCreationDate(new Date());
    }

    @Test
    void testCreatePacket() {
        when(packetService.createPacket(any(Packet.class))).thenReturn(samplePacket);

        ResponseEntity<Packet> response = packetController.createPacket(samplePacket);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(samplePacket, response.getBody());
        verify(packetService, times(1)).createPacket(any(Packet.class));
    }

    @Test
    void testGetPacketsByCustomer() {
        List<Packet> packets = new ArrayList<>();
        packets.add(samplePacket);
        when(packetService.getPacketsByCustomerId(anyLong())).thenReturn(packets);

        ResponseEntity<List<Packet>> response = packetController.getPacketsByCustomer(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(packets, response.getBody());
        verify(packetService, times(1)).getPacketsByCustomerId(anyLong());
    }

    @Test
    void testGetPacketsByCustomer_NoContent() {
        when(packetService.getPacketsByCustomerId(anyLong())).thenReturn(new ArrayList<>());

        ResponseEntity<List<Packet>> response = packetController.getPacketsByCustomer(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(packetService, times(1)).getPacketsByCustomerId(anyLong());
    }

    @Test
    void testValidatePacket() {
        samplePacket.setStatus(Status.IN_PROGRESS);
        when(packetService.getPacketById(anyLong())).thenReturn(samplePacket);

        ResponseEntity<String> response = packetController.validatePacket(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Packet validated successfully", response.getBody());
        verify(packetService, times(1)).validatePacket(samplePacket);
    }

    @Test
    void testValidatePacket_AlreadyConfirmed() {
        samplePacket.setStatus(Status.CONFIRMED);
        when(packetService.getPacketById(anyLong())).thenReturn(samplePacket);

        ResponseEntity<String> response = packetController.validatePacket(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Packet is already confirmed", response.getBody());
        verify(packetService, never()).validatePacket(samplePacket);
    }

    @Test
    void testCancelPacket_Success() {
        samplePacket.setStatus(Status.CONFIRMED);
        when(packetService.getPacketById(anyLong())).thenReturn(samplePacket);

        ResponseEntity<String> response = packetController.cancelPacket(1L, 1000L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Packet canceled successfully", response.getBody());
        verify(packetService, times(1)).cancelPacket(samplePacket, 1000L);
    }

    @Test
    void testCancelPacket_Failure() {
        samplePacket.setStatus(Status.CONFIRMED);
        when(packetService.getPacketById(anyLong())).thenReturn(samplePacket);
        doThrow(new RuntimeException("The packet cancellation period is over")).when(packetService).cancelPacket(any(Packet.class), anyLong());

        ResponseEntity<String> response = packetController.cancelPacket(1L, 1000L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The packet cancellation period is over", response.getBody());
        verify(packetService, times(1)).cancelPacket(any(Packet.class), anyLong());
    }

    @Test
    void testGetPacketStatus() {
        samplePacket.setStatus(Status.IN_PROGRESS);
        when(packetService.getPacketById(anyLong())).thenReturn(samplePacket);

        ResponseEntity<Status> response = packetController.getPacketStatus(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Status.IN_PROGRESS, response.getBody());
        verify(packetService, times(1)).getPacketById(anyLong());
    }

    @Test
    void testGetAllPackets() {
        List<Packet> packets = new ArrayList<>();
        packets.add(samplePacket);
        when(packetService.getAllPackets()).thenReturn(packets);

        ResponseEntity<List<Packet>> response = packetController.getAllPackets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(packets, response.getBody());
        verify(packetService, times(1)).getAllPackets();
    }

    @Test
    void testGetAllPackets_NoContent() {
        when(packetService.getAllPackets()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Packet>> response = packetController.getAllPackets();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(packetService, times(1)).getAllPackets();
    }
}
