package com.hotel.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.model.Booking;
import com.hotel.service.BookingService;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Map<String, Object> req) {
		Long roomId = Long.valueOf(req.get("roomId").toString());
		Long userId = Long.valueOf(req.get("userId").toString());
		Booking b = bookingService.createBooking(roomId, userId);
		return ResponseEntity.ok(Map.of("bookingId", b.getId(), "status", b.getStatus()));
	}

}
