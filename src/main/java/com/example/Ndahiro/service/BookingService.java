package com.example.Ndahiro.service;

import com.example.Ndahiro.dto.BookingRequest;
import com.example.Ndahiro.model.Booking;
import com.example.Ndahiro.model.Room;
import com.example.Ndahiro.model.User;
import com.example.Ndahiro.repository.BookingRepository;
import com.example.Ndahiro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomService roomService;
    private final ApplicationEventPublisher eventPublisher;
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
    
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + id));
    }
    
    @Transactional
    public Booking createBooking(BookingRequest bookingRequest, Long userId) {
        // Get the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        // Get the room
        Room room = roomService.getRoomById(bookingRequest.getRoomId());
        
        // Check if room is available
        LocalDate checkIn = bookingRequest.getCheckIn();
        LocalDate checkOut = bookingRequest.getCheckOut();
        
        List<Room> availableRooms = roomService.getAvailableRooms(
                room.getHotel().getId(), checkIn, checkOut);
        
        if (availableRooms.stream().noneMatch(r -> r.getId().equals(room.getId()))) {
            throw new RuntimeException("The room is not available for the selected dates");
        }
        
        // Create and save the booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckIn(checkIn);
        booking.setCheckOut(checkOut);
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        
        Booking savedBooking = bookingRepository.save(booking);
        
        // Publish an event for billing generation
        eventPublisher.publishEvent(new BookingCreatedEvent(savedBooking));
        
        return savedBooking;
    }
    
    @Transactional
    public void cancelBooking(Long id, Long userId) {
        Booking booking = getBookingById(id);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        // Check if the booking belongs to the user or user is admin
        if (!booking.getUser().getId().equals(userId) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("You are not authorized to cancel this booking");
        }
        
        // Check if the booking is already started
        if (booking.getCheckIn().isBefore(LocalDate.now()) || booking.getCheckIn().isEqual(LocalDate.now())) {
            throw new RuntimeException("Cannot cancel a booking that has already started");
        }
        
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
    
    // Event class for booking creation
    public static class BookingCreatedEvent {
        private final Booking booking;
        
        public BookingCreatedEvent(Booking booking) {
            this.booking = booking;
        }
        
        public Booking getBooking() {
            return booking;
        }
    }
} 