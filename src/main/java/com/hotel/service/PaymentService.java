package com.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.hotel.dto.PaymentEvent;
import com.hotel.model.Payment;
import com.hotel.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepo;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public Payment initiatePayment(Long bookingId, double amount) {
		Payment payment = new Payment(bookingId, amount, Payment.Status.PENDING);
		paymentRepo.save(payment);
		return payment;
	}

	public Payment verify3DS(Long paymentId, String otp) {
		Payment payment = paymentRepo.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));
		PaymentEvent event;
		if ("123456".equals(otp)) {
			payment.setStatus(Payment.Status.SUCCESS);
			event = new PaymentEvent(paymentId, "payment-captured", payment.getBookingId());
		} else {
			payment.setStatus(Payment.Status.FAILED);
			event = new PaymentEvent(paymentId, "payment-failed", payment.getBookingId());
		}

		paymentRepo.save(payment);
		kafkaTemplate.send("payment-events", event);

		return payment;
	}
}
