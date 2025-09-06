package com.hotel.dto;

public class PaymentEvent {
	private Long paymentId;
	private String eventType;
	private Long bookingId;

	public PaymentEvent() {
	}

	public PaymentEvent(Long paymentId, String eventType, Long bookingId) {
		this.paymentId = paymentId;
		this.eventType = eventType;
		this.bookingId = bookingId;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	@Override
	public String toString() {
		return "PaymentEvent [paymentId=" + paymentId + ", eventType=" + eventType + ", bookingId=" + bookingId + "]";
	}

}
