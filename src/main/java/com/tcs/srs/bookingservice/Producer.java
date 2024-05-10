package com.tcs.srs.bookingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
	private static final String TOPIC_PAYMENT = "payment";

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String message) {
		this.kafkaTemplate.send(TOPIC_PAYMENT, message);
	}

	public void sendPaymentProcessPayload(String message, String bookingNumber) {
		sendMessage(message + ":" + bookingNumber);
	}
}
