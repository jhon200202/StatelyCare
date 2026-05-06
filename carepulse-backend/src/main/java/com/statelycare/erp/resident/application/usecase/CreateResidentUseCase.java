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
public class CreateResidentUseCase {

    private final ResidentRepository repository;
    private final RoomRepository roomRepository;

    public CreateResidentUseCase(ResidentRepository repository, RoomRepository roomRepository) {
        this.repository = repository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public ResidentResponse execute(ResidentRequest request) {
        String residentCode = "RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        if (request.roomId() != null) {
            Room room = roomRepository.findById(request.roomId())
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

        Resident resident = Resident.createNew(
            residentCode,
            request.firstName(),
            request.lastName(),
            request.dateOfBirth(),
            request.gender(),
            request.roomId(),
            request.admissionDate()
        );

        Resident saved = repository.save(resident);
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