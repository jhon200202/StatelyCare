package com.statelycare.erp.resident.application.usecase;

import com.statelycare.erp.resident.domain.model.Resident;
import com.statelycare.erp.resident.domain.repository.ResidentRepository;
import com.statelycare.erp.room.domain.model.Room;
import com.statelycare.erp.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteResidentUseCase {

    private final ResidentRepository repository;
    private final RoomRepository roomRepository;

    public DeleteResidentUseCase(ResidentRepository repository, RoomRepository roomRepository) {
        this.repository = repository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public void execute(UUID id) {
        Resident resident = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Residente no encontrado"));

        // Liberar habitación si tenía una asignada
        if (resident.roomId() != null) {
            roomRepository.findById(resident.roomId()).ifPresent(room -> {
                Room updatedRoom = new Room(
                    room.id(),
                    room.roomNumber(),
                    room.wing(),
                    room.floor(),
                    room.capacity(),
                    Math.max(0, room.currentOccupancy() - 1),
                    room.roomType(),
                    room.isActive()
                );
                roomRepository.save(updatedRoom);
            });
        }

        repository.delete(resident);
    }
}