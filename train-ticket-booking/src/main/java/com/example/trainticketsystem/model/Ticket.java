package com.example.trainticketsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor  // Automatically generates the constructor
public class Ticket {
	
	private String from;
	private String to;
	private User user;
	private String section; // A or B
	private String seatNumber;
	private double price = 5.0;
	
	public Ticket(User user, String from, String to, int someValue) {
		this.user = user;
		this.from = from;
		this.to = to;
	}
	
}
