package com.statelycare.erp.room.application.usecase;

import com.statelycare.erp.room.domain.model.Room;
import com.statelycare.erp.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class DeactivateRoomUseCase {

    private final RoomRepository repository;

    public DeactivateRoomUseCase(RoomRepository repository) {
        this.repository = repository;
    }

    public void execute(UUID roomId) {
        Room existing = repository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        Room deactivated = new Room(
            existing.id(),
            existing.roomNumber(),
            existing.wing(),
            existing.floor(),
            existing.capacity(),
            existing.currentOccupancy(),
            existing.roomType(),
            false
        );

        repository.save(deactivated);
    }
}