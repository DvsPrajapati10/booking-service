package com.tcs.srs.bookingservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

	@Autowired
	BookingService bookingService;

	@Autowired
	PassengerService passengerService;

	Passenger passenger;

	@KafkaListener(topics = "booking", groupId = "group_id")
	public void consume(String message) {

		String[] payload = message.split(":");
		String bookingNumber = payload[1];
		bookingService.updateBookingWithStatus(payload[0], bookingNumber);

		passenger = new Passenger();
		passenger.setPassengerId(Integer.valueOf((int) (Math.random() * 100000)));
		passenger.setBookingNumber(bookingNumber);
		passengerService.createPassenger(passenger);
	}

}