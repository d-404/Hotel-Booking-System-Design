package com.hotel.kafka;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.model.Booking;
import com.hotel.repository.BookingRepository;

import jakarta.transaction.Transactional;

@Component
public class BookingEventConsumer {

	private final BookingRepository bookingRepo;
	private final ObjectMapper objectMapper = new ObjectMapper();

	public BookingEventConsumer(BookingRepository bookingRepo) {
		this.bookingRepo = bookingRepo;
	}

	@SuppressWarnings("unchecked")
	@KafkaListener(topics = "payment-events", groupId = "hotel-group")
	@Transactional
	public void handlePaymentEvents(String payload) {
		try {
			Map<String, Object> event = objectMapper.readValue(payload, Map.class);
			String eventType = (String) event.get("eventType");
			Long bookingId = Long.valueOf(event.get("bookingId").toString());

			bookingRepo.findById(bookingId).ifPresent(booking -> {
				switch (eventType) {
				case "payment-captured" -> booking.setStatus(Booking.Status.CONFIRMED);
				case "payment-failed" -> booking.setStatus(Booking.Status.FAILED);
				}
				bookingRepo.save(booking);
			});

			System.out.println("✅ Processed payment event: " + event);

		} catch (Exception e) {
			System.err.println("❌ Failed to process payment event: " + e.getMessage());
		}
	}
}
