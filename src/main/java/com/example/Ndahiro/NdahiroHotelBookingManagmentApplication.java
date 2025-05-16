package com.example.Ndahiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
public class NdahiroHotelBookingManagmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(NdahiroHotelBookingManagmentApplication.class, args);
	}
	
	// Disable any existing JwtAuthFilter by setting its order to -1
	@Bean
	public FilterRegistrationBean<OncePerRequestFilter> jwtAuthFilterRegistration() {
		FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
		
		// Create a dummy filter that does nothing
		registration.setFilter(new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
					throws ServletException, IOException {
				filterChain.doFilter(request, response);
			}
		});
		
		// Set a name that matches the JwtAuthFilter bean name pattern
		registration.setName("jwtAuthFilter");
		// Disable this filter by setting its order to -1
		registration.setEnabled(false);
		
		return registration;
	}
}
