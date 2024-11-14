package com.example.shoppingcart.controllers;

import com.example.shoppingcart.entities.Packet;
import com.example.shoppingcart.enums.Status;
import com.example.shoppingcart.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packets")
public class PacketController {

    private final PacketService packetService;

    @Autowired
    public PacketController(PacketService packetService) {
        this.packetService = packetService;
    }

    // Endpoint to create a new packet
    @PostMapping
    public ResponseEntity<Packet> createPacket(@RequestBody Packet packet) {
        Packet createdPacket = packetService.createPacket(packet);
        return new ResponseEntity<>(createdPacket, HttpStatus.CREATED);
    }

    // Endpoint to get all packets for a specific customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Packet>> getPacketsByCustomer(@PathVariable Long customerId) {
        List<Packet> packets = packetService.getPacketsByCustomerId(customerId);
        return packets.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(packets, HttpStatus.OK);
    }

    // Endpoint to validate a packet
    @PutMapping("/{packetId}/validate")
    public ResponseEntity<String> validatePacket(@PathVariable Long packetId) {
        Packet packet = packetService.getPacketById(packetId);
        if (packet == null) {
            return new ResponseEntity<>("Packet not found", HttpStatus.NOT_FOUND);
        }
        if (Status.CONFIRMED.equals(packet.getStatus())) {
            return new ResponseEntity<>("Packet is already confirmed", HttpStatus.BAD_REQUEST);
        }
        packetService.validatePacket(packet);
        return new ResponseEntity<>("Packet validated successfully", HttpStatus.OK);
    }

    // Endpoint to cancel a packet within the cancellation period
    @PutMapping("/{packetId}/cancel")
    public ResponseEntity<String> cancelPacket(@PathVariable Long packetId, @RequestParam Long packetTimeLimit) {
        Packet packet = packetService.getPacketById(packetId);
        if (packet == null) {
            return new ResponseEntity<>("Packet not found", HttpStatus.NOT_FOUND);
        }
        try {
            packetService.cancelPacket(packet, packetTimeLimit);
            return new ResponseEntity<>("Packet canceled successfully", HttpStatus.OK);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Endpoint to get the status of a packet
    @GetMapping("/{packetId}/status")
    public ResponseEntity<Status> getPacketStatus(@PathVariable Long packetId) {
        Packet packet = packetService.getPacketById(packetId);
        if (packet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(packet.getStatus(), HttpStatus.OK);
    }

    // Endpoint to list all packets
    @GetMapping
    public ResponseEntity<List<Packet>> getAllPackets() {
        List<Packet> packets = packetService.getAllPackets();
        return packets.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(packets, HttpStatus.OK);
    }
}
