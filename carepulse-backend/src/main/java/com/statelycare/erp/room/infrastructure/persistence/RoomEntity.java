package com.statelycare.erp.room.infrastructure.persistence;

import com.statelycare.erp.room.domain.model.RoomType;
import com.statelycare.erp.room.domain.model.Wing;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "rooms")
public class RoomEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "room_number", nullable = false)
    private String roomNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Wing wing;
    
    @Column(nullable = false)
    private int floor;
    
    @Column(nullable = false)
    private int capacity;
    
    @Column(name = "current_occupancy", nullable = false)
    private int currentOccupancy;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public RoomEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public Wing getWing() { return wing; }
    public void setWing(Wing wing) { this.wing = wing; }
    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(int currentOccupancy) { this.currentOccupancy = currentOccupancy; }
    public RoomType getRoomType() { return roomType; }
    public void setRoomType(RoomType roomType) { this.roomType = roomType; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}