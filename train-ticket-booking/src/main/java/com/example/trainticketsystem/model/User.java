package com.example.trainticketsystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Generates a no-arguments constructor
@AllArgsConstructor // Generates a constructor with all arguments
@Builder  // Enables the builder pattern for creating instances of User
public class User {
	
	private String firstName;
	private String lastName;
	private String email;
	
}
