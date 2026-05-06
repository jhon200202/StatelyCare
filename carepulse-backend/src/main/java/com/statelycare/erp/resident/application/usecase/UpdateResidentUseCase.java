package com.statelycare.erp.resident.application.usecase;

import com.statelycare.erp.resident.application.dto.ResidentRequest;
import com.statelycare.erp.resident.application.dto.ResidentResponse;
import com.statelycare.erp.resident.domain.model.Resident;
import com.statelycare.erp.resident.domain.repository.ResidentRepository;
import com.statelycare.erp.room.domain.model.Room;
import com.statelycare.erp.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateResidentUseCase {

    private final ResidentRepository repository;
    private final RoomRepository roomRepository;

    public UpdateResidentUseCase(ResidentRepository repository, RoomRepository roomRepository) {
        this.repository = repository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public ResidentResponse execute(UUID id, ResidentRequest request) {
        Resident existing = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Residente no encontrado"));

        UUID oldRoomId = existing.roomId();
        UUID newRoomId = request.roomId();

        // Si cambia de habitación, actualizar ocupaciones
        if (!java.util.Objects.equals(oldRoomId, newRoomId)) {
            // Liberar habitación anterior
            if (oldRoomId != null) {
                roomRepository.findById(oldRoomId).ifPresent(room -> {
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

            // Ocupar nueva habitación
            if (newRoomId != null) {
                Room room = roomRepository.findById(newRoomId)
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

                if (room.currentOccupancy() >= room.capacity()) {
                    throw new RuntimeException("La habitación está llena");
                }

                Room updatedRoom = new Room(
                    room.id(),
                    room.roomNumber(),
                    room.wing(),
                    room.floor(),
                    room.capacity(),
                    room.currentOccupancy() + 1,
                    room.roomType(),
                    room.isActive()
                );
                roomRepository.save(updatedRoom);
            }
        }

        Resident updated = existing.update(
            request.firstName(),
            request.lastName(),
            request.dateOfBirth(),
            request.gender(),
            newRoomId,
            request.admissionDate()
        );

        Resident saved = repository.save(updated);
        return mapToResponse(saved);
    }

    private ResidentResponse mapToResponse(Resident r) {
        return new ResidentResponse(
            r.id(),
            r.residentCode(),
            r.firstName(),
            r.lastName(),
            r.dateOfBirth(),
            r.gender(),
            r.roomId(),
            r.admissionDate(),
            r.status()
        );
    }
}