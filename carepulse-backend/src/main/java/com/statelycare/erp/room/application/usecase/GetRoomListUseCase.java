package com.statelycare.erp.room.application.usecase;

import com.statelycare.erp.room.application.dto.RoomResponse;
import com.statelycare.erp.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GetRoomListUseCase {

    private final RoomRepository repository;

    public GetRoomListUseCase(RoomRepository repository) {
        this.repository = repository;
    }

    public List<RoomResponse> execute() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private RoomResponse toResponse(com.statelycare.erp.room.domain.model.Room room) {
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