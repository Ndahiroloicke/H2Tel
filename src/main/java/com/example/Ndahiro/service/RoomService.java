package com.example.Ndahiro.service;

import com.example.Ndahiro.dto.RoomRequest;
import com.example.Ndahiro.model.Hotel;
import com.example.Ndahiro.model.Room;
import com.example.Ndahiro.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final HotelService hotelService;
    
    public List<Room> getRoomsByHotelId(Long hotelId) {
        return roomRepository.findByHotelId(hotelId);
    }
    
    public List<Room> getAvailableRooms(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findAvailableRooms(hotelId, checkIn, checkOut);
    }
    
    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found with id: " + id));
    }
    
    public Room createRoom(RoomRequest roomRequest) {
        Hotel hotel = hotelService.getHotelById(roomRequest.getHotelId());
        
        Room room = new Room();
        room.setHotel(hotel);
        room.setRoomType(roomRequest.getRoomType());
        room.setPrice(roomRequest.getPrice());
        room.setAvailable(roomRequest.isAvailable());
        
        return roomRepository.save(room);
    }
    
    public Room updateRoom(Long id, RoomRequest roomRequest) {
        Room room = getRoomById(id);
        Hotel hotel = hotelService.getHotelById(roomRequest.getHotelId());
        
        room.setHotel(hotel);
        room.setRoomType(roomRequest.getRoomType());
        room.setPrice(roomRequest.getPrice());
        room.setAvailable(roomRequest.isAvailable());
        
        return roomRepository.save(room);
    }
    
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new EntityNotFoundException("Room not found with id: " + id);
        }
        
        roomRepository.deleteById(id);
    }
} 
 