package com.tcs.srs.bookingservice;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

	@Autowired
	public BookingRepository bookingRepository;

	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	public Booking insertBooking(Booking bookingObj) {
		return bookingRepository.save(bookingObj);
	}

	public int updateBooking(Booking bookingObj) {
		return bookingRepository.updateBookingByNumber(bookingObj.getBookingDate(), bookingObj.getSource(),
				bookingObj.getDestination(), bookingObj.getNumberOfSeats(), bookingObj.getStatus(),
				bookingObj.getBusNumber(), bookingObj.getBookingNumber());
	}

	public void updateBookingWithStatus(String status, String bookingNumber) {
		bookingRepository.updateBookingStatusByBookingNumber(status, bookingNumber);
	}

	public Optional<Booking> getBookingById(String bookingNumber) {
		return bookingRepository.findById(bookingNumber);
	}

	public int deleteBooking(String bookingNumber) {
		return bookingRepository.deleteByBookingNumber(bookingNumber);
	}

}
