package com.hotel.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.model.Payment;
import com.hotel.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentContoller {
	private final PaymentService paymentService;

	public PaymentContoller(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping("/initiate")
	public ResponseEntity<?> initiate(@RequestBody Map<String, Object> req) {
		Long bookingId = Long.valueOf(req.get("bookingId").toString());
		double amount = Double.parseDouble(req.get("amount").toString());
		Payment resp = paymentService.initiatePayment(bookingId, amount);
		return ResponseEntity.ok(resp);
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verify(@RequestBody Map<String, Object> req) {
		Long paymentId = Long.valueOf(req.get("paymentId").toString());
		String otp = req.get("otp").toString();
		Payment resp = paymentService.verify3DS(paymentId, otp);
		return ResponseEntity.ok(resp);
	}

}
