package com.example.Ndahiro.controller;

import com.example.Ndahiro.model.Billing;
import com.example.Ndahiro.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billings")
@RequiredArgsConstructor
public class BillingController {
    
    private final BillingService billingService;
    
    @GetMapping("/{bookingId}")
    public ResponseEntity<Billing> getBillingByBookingId(
            @PathVariable Long bookingId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(billingService.getBillingByBookingId(bookingId, userId));
    }
} 