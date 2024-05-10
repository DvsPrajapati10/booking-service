package com.tcs.srs.bookingservice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
public class MainRestController {

	@Autowired
	BookingService bookingService;
	@Autowired
	PassengerService passengerService;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	Producer producer;

	Booking booking;
	Bus bus;
	BusRoute busRoute;

	@GetMapping("/get/all/bookings")
	public ResponseEntity<List<Booking>> getAllBookings() {
		return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
	}

	@GetMapping("/get/all/passengers")
	public ResponseEntity<List<Passenger>> getAlPassengers() {
		return new ResponseEntity<>(passengerService.getAllPassengers(), HttpStatus.OK);
	}

	@GetMapping("/get/booking/{bookingNumber}")
	public ResponseEntity<Optional<Booking>> getBookingByBookingNumber(@PathVariable String bookingNumber) {
		return new ResponseEntity<>(bookingService.getBookingById(bookingNumber), HttpStatus.OK);
	}

	@PostMapping("/createBooking")
	public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
		return new ResponseEntity<>(bookingService.insertBooking(booking), HttpStatus.CREATED);
	}

	@PutMapping("updateBooking/{bookingNumber}")
	public ResponseEntity<Integer> updateBookingByNumber(@PathVariable String bookingNumber,
			@RequestBody Booking booking) {
		if (bookingService.getBookingById(bookingNumber).isPresent()) {
			return new ResponseEntity<>(bookingService.updateBooking(booking), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("deleteBooking/{bookingNumber}")
	public ResponseEntity<Integer> deleteBookingByNumber(@PathVariable String bookingNumber) {
		if (bookingService.getBookingById(bookingNumber).isPresent()) {
			return new ResponseEntity<>(bookingService.deleteBooking(bookingNumber), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/reserve/seats/bus")
	public ResponseEntity<Booking> reserveSeatsForBus(@RequestParam Integer noOfSeats,
			@RequestParam Integer busNumber) {
		this.booking = new Booking();
		this.busRoute = callAdminServiceGetBusRouteByNumber(busNumber);
		this.bus = callInventoryServiceGetBusByNumber(busNumber);
		if (this.bus.getAvailableSeats() >= noOfSeats) {
			updateBookingAndInsert(noOfSeats, busNumber);
			producer.sendPaymentProcessPayload("SUCCESS", booking.getBookingNumber());
			return new ResponseEntity<>(this.booking, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	public Bus callInventoryServiceGetBusByNumber(Integer busNumber) {
		return webClientBuilder.build().get()
				.uri("http://host.docker.internal:8072/inventory-service/api/v1/get/bus/" + busNumber).retrieve()
				.onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new RuntimeException("Bad Request")))
				.onStatus(HttpStatus::is5xxServerError, response -> Mono.just(new RuntimeException("Server Error")))
				.bodyToMono(Bus.class).block();
	}

	public Bus updateBusByBusNumber(Integer busNumber, Integer numberSeatsLeftAfterBooking) {
		return webClientBuilder.build().put()
				.uri("http://host.docker.internal:8072/inventory-service/api/v1/get/bus/" + busNumber).retrieve()
				.onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new RuntimeException("Bad Request")))
				.onStatus(HttpStatus::is5xxServerError, response -> Mono.just(new RuntimeException("Server Error")))
				.bodyToMono(Bus.class).block();
	}

	public BusRoute callAdminServiceGetBusRouteByNumber(Integer busNumber) {
		return webClientBuilder.build().get()
				.uri("http://host.docker.internal:8072/admin-service/api/v1/get/busroute/" + busNumber).retrieve()
				.onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new RuntimeException("Bad Request")))
				.onStatus(HttpStatus::is5xxServerError, response -> Mono.just(new RuntimeException("Server Error")))
				.bodyToMono(BusRoute.class).block();
	}

	private void updateBookingAndInsert(Integer noOfSeats, Integer busNumber) {
		this.booking.setBusNumber(busNumber);
		this.booking.setBookingNumber(String.valueOf((int) (Math.random() * 100000)));
		this.booking.setBookingDate(LocalDate.now());
		this.booking.setSource(this.busRoute.getSource());
		this.booking.setDestination(this.busRoute.getDestination());
		this.booking.setNumberOfSeats(noOfSeats);
		this.booking.setStatus("PENDING");
		bookingService.insertBooking(this.booking);
	}

}
