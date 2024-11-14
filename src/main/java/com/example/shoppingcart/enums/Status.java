package com.example.shoppingcart.enums;

public enum Status {

    IN_PROGRESS,      // Order is in progress and yet to be confirmed.
    CONFIRMED,        // Order has been confirmed and is awaiting fulfillment.
    PACKAGED,         // Items have been packaged and are ready for shipping.
    SHIPPED,          // Order has been shipped out and is in transit.
    OUT_FOR_DELIVERY, // Package is out for delivery to the customer's address.
    DELIVERED,        // Package has been delivered to the customer.
    RETURN_REQUESTED, // Customer has requested a return.
    RETURNED,         // Package has been returned to the warehouse.
    REFUNDED,         // Order has been refunded to the customer.
    CANCELED          // Order has been canceled.
}