package com.hotel.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

	@Bean
	NewTopic bookingEventsTopic() {
		return new NewTopic("booking-events", 3, (short) 1);
	}

	@Bean
	NewTopic paymentEventsTopic() {
		return new NewTopic("payment-events", 3, (short) 1);
	}
}
