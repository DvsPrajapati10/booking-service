package com.tcs.srs.bookingservice;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

	@Transactional
	@Modifying
	@Query("delete from Booking b where b.bookingNumber = ?1")
	int deleteByBookingNumber(String bookingNumber);

	@Transactional
	@Modifying
	@Query("update Booking b set b.bookingDate = ?1, b.source = ?2, b.destination = ?3, b.numberOfSeats = ?4, b.status = ?5, b.busNumber = ?6 "
			+ "where b.bookingNumber = ?7")
	int updateBookingByNumber(LocalDate bookingDate, String source, String destination, Integer numberOfSeats,
			String status, Integer busNumber, String bookingNumber);

	@Transactional
	@Modifying
	@Query("update Booking b set b.status = ?1 where b.bookingNumber = ?2")
	int updateBookingStatusByBookingNumber(String status, String bookingNumber);

}