package com.statelycare.erp.room.application.usecase;

import com.statelycare.erp.room.application.dto.RoomRequest;
import com.statelycare.erp.room.application.dto.RoomResponse;
import com.statelycare.erp.room.domain.model.Room;
import com.statelycare.erp.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateRoomUseCase {

    private final RoomRepository repository;

    public CreateRoomUseCase(RoomRepository repository) {
        this.repository = repository;
    }

    public RoomResponse execute(RoomRequest request) {
        Room room = Room.createNew(
            request.roomNumber(),
            request.wing(),
            request.floor(),
            request.capacity(),
            request.roomType()
        );
        
        Room saved = repository.save(room);
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