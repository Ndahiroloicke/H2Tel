package com.example.Ndahiro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {
    
    @NotBlank(message = "Hotel name is required")
    private String name;
    
    @NotBlank(message = "Hotel location is required")
    private String location;
} 