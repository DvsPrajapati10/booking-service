package com.tcs.srs.bookingservice;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@Column(name = "booking_number", nullable = false, length = 20)
	private String bookingNumber;

	@Column(name = "booking_date")
	private LocalDate bookingDate;

	@Column(name = "source", length = 20)
	private String source;

	@Column(name = "destination", length = 20)
	private String destination;

	@Column(name = "number_of_seats")
	private Integer numberOfSeats;

	@Column(name = "status", length = 20)
	private String status;

	@Column(name = "bus_number")
	private Integer busNumber;

}