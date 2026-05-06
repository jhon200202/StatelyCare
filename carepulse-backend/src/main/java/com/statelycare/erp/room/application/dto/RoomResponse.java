package com.statelycare.erp.room.application.dto;

import com.statelycare.erp.room.domain.model.RoomType;
import com.statelycare.erp.room.domain.model.Wing;
import java.util.UUID;

public record RoomResponse(
    UUID id,
    String roomNumber,
    Wing wing,
    int floor,
    int capacity,
    int currentOccupancy,
    RoomType roomType,
    boolean isActive
) {}