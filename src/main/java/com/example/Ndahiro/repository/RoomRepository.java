package com.example.Ndahiro.repository;

import com.example.Ndahiro.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByHotelId(Long hotelId);
    
    List<Room> findByHotelIdAndIsAvailable(Long hotelId, boolean isAvailable);
    
    @Query("""
            SELECT r FROM Room r
            WHERE r.id NOT IN (
                SELECT b.room.id FROM Booking b
                WHERE b.status != 'CANCELLED'
                AND (
                    (:checkIn BETWEEN b.checkIn AND b.checkOut)
                    OR (:checkOut BETWEEN b.checkIn AND b.checkOut)
                    OR (b.checkIn BETWEEN :checkIn AND :checkOut)
                )
            )
            AND r.hotel.id = :hotelId
            """)
    List<Room> findAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut);
} 