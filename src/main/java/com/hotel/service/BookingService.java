package com.hotel.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.hotel.kafka.BookingEventProducer;
import com.hotel.model.Booking;
import com.hotel.repository.BookingRepository;
import com.hotel.util.RedisLockUtil;

import jakarta.transaction.Transactional;

@Service
public class BookingService {

	private final BookingRepository bookingRepo;
	private final BookingEventProducer producer;
	private final RedisLockUtil redisLockUtil;

	public BookingService(BookingRepository bookingRepo, BookingEventProducer producer, RedisLockUtil redisLockUtil) {
		this.bookingRepo = bookingRepo;
		this.producer = producer;
		this.redisLockUtil = redisLockUtil;
	}

	@Transactional
	public Booking createBooking(Long roomId, Long userId) {
		String lockKey = "room:" + roomId;
		String lockVal = UUID.randomUUID().toString();
		if (!redisLockUtil.tryLock(lockKey, lockVal, 15000)) {
			throw new RuntimeException("Room locked - try again");
		}
		try {
			Booking b = new Booking(roomId, userId, Booking.Status.PENDING);
			Booking saved = bookingRepo.save(b);
			producer.sendBookingCreated(saved);
			return saved;
		} finally {
			redisLockUtil.unlock(lockKey, lockVal);
		}
	}
}
