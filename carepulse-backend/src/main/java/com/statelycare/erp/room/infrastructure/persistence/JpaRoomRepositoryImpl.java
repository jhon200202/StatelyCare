package com.statelycare.erp.room.infrastructure.persistence;

import com.statelycare.erp.room.domain.model.Room;
import com.statelycare.erp.room.domain.model.RoomType;
import com.statelycare.erp.room.domain.model.Wing;
import com.statelycare.erp.room.domain.repository.RoomRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaRoomRepositoryImpl implements RoomRepository {

    private final SpringDataRoomRepository repository;

    public JpaRoomRepositoryImpl(SpringDataRoomRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Room> findAll() {
        return repository.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Room> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Room save(Room room) {
        return toDomain(repository.save(toEntity(room)));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<Room> findByIsActive(boolean isActive) {
        return repository.findAll().stream()
                .filter(e -> e.isActive() == isActive)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Room toDomain(RoomEntity entity) {
        return new Room(
            entity.getId(),
            entity.getRoomNumber(),
            entity.getWing(),
            entity.getFloor(),
            entity.getCapacity(),
            entity.getCurrentOccupancy(),
            entity.getRoomType(),
            entity.isActive()
        );
    }

    private RoomEntity toEntity(Room domain) {
        RoomEntity entity = new RoomEntity();
        entity.setId(domain.id());
        entity.setRoomNumber(domain.roomNumber());
        entity.setWing(domain.wing());
        entity.setFloor(domain.floor());
        entity.setCapacity(domain.capacity());
        entity.setCurrentOccupancy(domain.currentOccupancy());
        entity.setRoomType(domain.roomType());
        entity.setActive(domain.isActive());
        return entity;
    }
}