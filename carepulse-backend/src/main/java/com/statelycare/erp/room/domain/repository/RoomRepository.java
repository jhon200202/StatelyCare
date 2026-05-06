package com.statelycare.erp.room.domain.repository;

import com.statelycare.erp.room.domain.model.Room;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {
    List<Room> findAll();
    Optional<Room> findById(UUID id);
    Room save(Room room);
    void deleteById(UUID id);
    long count();
    List<Room> findByIsActive(boolean isActive);
}