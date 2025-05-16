package com.example.Ndahiro.service;

import com.example.Ndahiro.model.Billing;
import com.example.Ndahiro.model.Booking;
import com.example.Ndahiro.model.User;
import com.example.Ndahiro.repository.BillingRepository;
import com.example.Ndahiro.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BillingService {
    
    private final BillingRepository billingRepository;
    private final BookingRepository bookingRepository;
    
    public Billing getBillingByBookingId(Long bookingId, Long userId) {
        // Check if the authenticated user is authorized to view this billing
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found with id: " + bookingId));
                
        User bookingUser = booking.getUser();
        
        if (!bookingUser.getId().equals(userId) && 
                bookingUser.getRole() != com.example.Ndahiro.model.User.Role.ADMIN) {
            throw new RuntimeException("You are not authorized to view this billing");
        }
        
        return billingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Billing not found for booking id: " + bookingId));
    }
    
    // This method is called when a BookingCreatedEvent is published
    @EventListener
    @Transactional
    public void handleBookingCreatedEvent(BookingService.BookingCreatedEvent event) {
        generateBilling(event.getBooking());
    }
    
    // This method would be called by a database trigger in a real environment
    // For demo purposes, we manually create a billing record when a booking is created
    private Billing generateBilling(Booking booking) {
        Billing billing = new Billing();
        billing.setBooking(booking);
        billing.setGeneratedAt(LocalDateTime.now());
        
        // Calculate the total amount based on room price and number of days
        long days = ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        double amount = booking.getRoom().getPrice() * days;
        billing.setAmount(amount);
        
        return billingRepository.save(billing);
    }
} 