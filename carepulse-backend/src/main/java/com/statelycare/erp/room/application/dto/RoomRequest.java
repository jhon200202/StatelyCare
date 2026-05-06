package com.statelycare.erp.room.application.dto;

import com.statelycare.erp.room.domain.model.RoomType;
import com.statelycare.erp.room.domain.model.Wing;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomRequest(
    @NotBlank String roomNumber,
    @NotNull Wing wing,
    @NotNull @Min(1) int floor,
    @NotNull @Min(1) int capacity,
    @NotNull RoomType roomType
) {}