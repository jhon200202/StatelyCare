package com.statelycare.erp.room.domain.model;

import java.util.UUID;

public record Room(
    UUID id,
    String roomNumber,
    Wing wing,
    int floor,
    int capacity,
    int currentOccupancy,
    RoomType roomType,
    boolean isActive
) {
    public static Room createNew(
            String roomNumber, 
            Wing wing, 
            int floor, 
            int capacity, 
            RoomType roomType) {
        return new Room(
            UUID.randomUUID(),
            roomNumber,
            wing,
            floor,
            capacity,
            0,
            roomType,
            true
        );
    }
}