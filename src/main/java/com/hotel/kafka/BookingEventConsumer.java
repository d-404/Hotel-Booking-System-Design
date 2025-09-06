package com.hotel.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.hotel.dto.PaymentEvent;
import com.hotel.model.Booking;
import com.hotel.repository.BookingRepository;

import jakarta.transaction.Transactional;

@Component
public class BookingEventConsumer {

	private final BookingRepository bookingRepo;

	public BookingEventConsumer(BookingRepository bookingRepo) {
		this.bookingRepo = bookingRepo;
	}

	@KafkaListener(topics = "payment-events", groupId = "hotel-group")
	@Transactional
	public void handlePaymentEvents(PaymentEvent paymentEvent) {
		try {
			String eventType = paymentEvent.getEventType();
			Long bookingId = paymentEvent.getBookingId();

			bookingRepo.findById(bookingId).ifPresent(booking -> {
				switch (eventType) {
				case "payment-captured" -> booking.setStatus(Booking.Status.CONFIRMED);
				case "payment-failed" -> booking.setStatus(Booking.Status.FAILED);
				}
				bookingRepo.save(booking);
			});

			System.out.println("✅ Processed payment event: " + paymentEvent);

		} catch (Exception e) {
			System.err.println("❌ Failed to process payment event: " + e.getMessage());
		}
	}
}
