package com.example.Ndahiro.service;

import com.example.Ndahiro.dto.HotelRequest;
import com.example.Ndahiro.model.Hotel;
import com.example.Ndahiro.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {
    
    private final HotelRepository hotelRepository;
    
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }
    
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found with id: " + id));
    }
    
    public Hotel createHotel(HotelRequest hotelRequest) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelRequest.getName());
        hotel.setLocation(hotelRequest.getLocation());
        
        return hotelRepository.save(hotel);
    }
    
    public Hotel updateHotel(Long id, HotelRequest hotelRequest) {
        Hotel hotel = getHotelById(id);
        
        hotel.setName(hotelRequest.getName());
        hotel.setLocation(hotelRequest.getLocation());
        
        return hotelRepository.save(hotel);
    }
    
    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new EntityNotFoundException("Hotel not found with id: " + id);
        }
        
        hotelRepository.deleteById(id);
    }
} 