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
@Table(name = "bus_route")
public class BusRoute {

	@Id
	@Column(name = "bus_number", nullable = false)
	private Integer id;

	@Column(name = "source", length = 20)
	private String source;

	@Column(name = "destination", length = 20)
	private String destination;

	@Column(name = "price")
	private Integer price;

	@Column(name = "route_id")
	private Integer routeId;

}