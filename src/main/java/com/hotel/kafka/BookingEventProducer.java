package com.hotel.kafka;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.hotel.model.Booking;

@Component
public class BookingEventProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public BookingEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendBookingCreated(Booking booking) {
		Map<String, Object> e = new HashMap<>();
		e.put("eventType", "booking-created");
		e.put("bookingId", booking.getId());
		e.put("roomId", booking.getRoomId());
		kafkaTemplate.send("booking-events", e);
	}
}
