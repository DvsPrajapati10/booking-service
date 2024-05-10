package com.tcs.srs.bookingservice;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "passenger")
public class Passenger {

	@Id
	@Column(name = "passenger_id", nullable = false)
	private Integer passengerId;

	@Column(name = "booking_number", nullable = false)
	private String bookingNumber;

}