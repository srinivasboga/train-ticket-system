package com.example.trainticketsystem.controller;

import com.example.trainticketsystem.model.Ticket;
import com.example.trainticketsystem.model.User;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class TicketController {
	
	private final Map<String, Ticket> ticketStore = new ConcurrentHashMap<>();
	private final Queue<String> sectionASeats = new LinkedList<>();
	private final Queue<String> sectionBSeats = new LinkedList<>();
	
	public TicketController() {
		for (int i = 1; i <= 10; i++) {
			sectionASeats.offer("A" + i);
			sectionBSeats.offer("B" + i);
		}
	}
	
	@PostMapping("/tickets")
	public Ticket purchaseTicket(@RequestBody User user) {
		String seat = sectionASeats.poll();
		String section = "A";
		if (seat == null) {
			seat = sectionBSeats.poll();
			section = "B";
		}
		if (seat == null) throw new RuntimeException("No seats available");
		
		Ticket ticket = new Ticket("London", "France", user, section, seat, 5.0);
		ticketStore.put(user.getEmail(), ticket);
		return ticket;
	}
	
	@GetMapping("/tickets/{email}")
	public Ticket getReceipt(@PathVariable String email) {
		return ticketStore.get(email);
	}
	
	@GetMapping("/sections/{section}/users")
	public List<Ticket> getUsersBySection(@PathVariable String section) {
		List<Ticket> usersInSection = new ArrayList<>();
		for (Ticket ticket : ticketStore.values()) {
			if (ticket.getSection().equalsIgnoreCase(section)) {
				usersInSection.add(ticket);
			}
		}
		return usersInSection;
	}
	
	@DeleteMapping("/tickets/{email}")
	public String removeUser(@PathVariable String email) {
		Ticket ticket = ticketStore.remove(email);
		if (ticket != null) {
			if (ticket.getSection().equals("A")) sectionASeats.offer(ticket.getSeatNumber());
			else sectionBSeats.offer(ticket.getSeatNumber());
			return "User removed";
		}
		return "User not found";
	}
	
	@PutMapping("/tickets/{email}/seat")
	public Ticket modifySeat(@PathVariable String email, @RequestParam String newSection) {
		Ticket ticket = ticketStore.get(email);
		if (ticket == null) throw new RuntimeException("Ticket not found");
		
		String newSeat = ("A".equalsIgnoreCase(newSection) ? sectionASeats.poll() : sectionBSeats.poll());
		if (newSeat == null) throw new RuntimeException("No seat available in section " + newSection);
		
		if (ticket.getSection().equals("A")) sectionASeats.offer(ticket.getSeatNumber());
		else sectionBSeats.offer(ticket.getSeatNumber());
		
		ticket.setSection(newSection);
		ticket.setSeatNumber(newSeat);
		return ticket;
	}
}