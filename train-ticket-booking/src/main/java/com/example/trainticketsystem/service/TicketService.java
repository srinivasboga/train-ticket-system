package com.example.trainticketsystem.service;

import com.example.trainticketsystem.model.Ticket;
import com.example.trainticketsystem.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class TicketService {
	
	private final Map<String, Ticket> ticketStore = new ConcurrentHashMap<>();
	private final Queue<String> sectionASeats = new ConcurrentLinkedQueue<>();
	private final Queue<String> sectionBSeats = new ConcurrentLinkedQueue<>();
	
	public TicketService() {
		for (int i = 1; i <= 10; i++) {
			sectionASeats.offer("A" + i);
			sectionBSeats.offer("B" + i);
		}
	}
	
	public Ticket purchaseTicket(User user) {
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
	
	public Ticket getTicket(String email) {
		return ticketStore.get(email);
	}
	
	public List<Ticket> getUsersBySection(String section) {
		List<Ticket> result = new ArrayList<>();
		for (Ticket ticket : ticketStore.values()) {
			if (ticket.getSection().equalsIgnoreCase(section)) {
				result.add(ticket);
			}
		}
		return result;
	}
	
	public String removeTicket(String email) {
		Ticket ticket = ticketStore.remove(email);
		if (ticket != null) {
			if ("A".equals(ticket.getSection())) sectionASeats.offer(ticket.getSeatNumber());
			else sectionBSeats.offer(ticket.getSeatNumber());
			return "User removed";
		}
		return "User not found";
	}
	
	public Ticket modifySeat(String email, String newSection) {
		Ticket ticket = ticketStore.get(email);
		if (ticket == null) throw new RuntimeException("Ticket not found");
		
		String newSeat = ("A".equalsIgnoreCase(newSection) ? sectionASeats.poll() : sectionBSeats.poll());
		if (newSeat == null) throw new RuntimeException("No seat available in section " + newSection);
		
		if ("A".equals(ticket.getSection())) sectionASeats.offer(ticket.getSeatNumber());
		else sectionBSeats.offer(ticket.getSeatNumber());
		
		ticket.setSection(newSection);
		ticket.setSeatNumber(newSeat);
		return ticket;
	}
}
