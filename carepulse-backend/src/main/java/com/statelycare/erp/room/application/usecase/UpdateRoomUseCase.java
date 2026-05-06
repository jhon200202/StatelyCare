package com.statelycare.erp.room.application.usecase;

import com.statelycare.erp.room.application.dto.RoomRequest;
import com.statelycare.erp.room.application.dto.RoomResponse;
import com.statelycare.erp.room.domain.model.Room;
import com.statelycare.erp.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UpdateRoomUseCase {

    private final RoomRepository repository;

    public UpdateRoomUseCase(RoomRepository repository) {
        this.repository = repository;
    }

    public RoomResponse execute(UUID roomId, RoomRequest request) {
        Room existing = repository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        Room updated = new Room(
            existing.id(),
            request.roomNumber(),
            request.wing(),
            request.floor(),
            request.capacity(),
            existing.currentOccupancy(),
            request.roomType(),
            existing.isActive()
        );

        Room saved = repository.save(updated);
        return toResponse(saved);
    }

    private RoomResponse toResponse(Room room) {
        return new RoomResponse(
            room.id(),
            room.roomNumber(),
            room.wing(),
            room.floor(),
            room.capacity(),
            room.currentOccupancy(),
            room.roomType(),
            room.isActive()
        );
    }
}