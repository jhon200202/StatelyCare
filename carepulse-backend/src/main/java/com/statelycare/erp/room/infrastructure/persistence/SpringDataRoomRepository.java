package com.statelycare.erp.room.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface SpringDataRoomRepository extends JpaRepository<RoomEntity, UUID> {
}