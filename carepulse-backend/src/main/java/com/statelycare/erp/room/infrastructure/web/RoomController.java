package com.statelycare.erp.room.infrastructure.web;

import com.statelycare.erp.room.application.dto.RoomRequest;
import com.statelycare.erp.room.application.dto.RoomResponse;
import com.statelycare.erp.room.application.usecase.CreateRoomUseCase;
import com.statelycare.erp.room.application.usecase.DeactivateRoomUseCase;
import com.statelycare.erp.room.application.usecase.GetRoomListUseCase;
import com.statelycare.erp.room.application.usecase.UpdateRoomUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final GetRoomListUseCase getRoomListUseCase;
    private final CreateRoomUseCase createRoomUseCase;
    private final UpdateRoomUseCase updateRoomUseCase;
    private final DeactivateRoomUseCase deactivateRoomUseCase;

    public RoomController(GetRoomListUseCase getRoomListUseCase, 
                          CreateRoomUseCase createRoomUseCase,
                          UpdateRoomUseCase updateRoomUseCase,
                          DeactivateRoomUseCase deactivateRoomUseCase) {
        this.getRoomListUseCase = getRoomListUseCase;
        this.createRoomUseCase = createRoomUseCase;
        this.updateRoomUseCase = updateRoomUseCase;
        this.deactivateRoomUseCase = deactivateRoomUseCase;
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(getRoomListUseCase.execute());
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomRequest request) {
        return new ResponseEntity<>(createRoomUseCase.execute(request), HttpStatus.CREATED);
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable UUID roomId, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(updateRoomUseCase.execute(roomId, request));
    }

    @PutMapping("/{roomId}/deactivate")
    public ResponseEntity<Void> deactivateRoom(@PathVariable UUID roomId) {
        deactivateRoomUseCase.execute(roomId);
        return ResponseEntity.noContent().build();
    }
}